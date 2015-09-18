# CouchbaseSyncTest

0. Create foo_bar bucket and add bucket to sync-gateway config.json
1. Change sync-gateway ip in CouchbaseLiteUtil.java
2. Compile and run app
3. Verify document with attachment is created and synced to couchbase server
4. Click button 'CAUSE EXCEPTION'
5. Observe exception in logcat

```
09-18 03:40:56.573  32004-32029/com.samples.couchbaselitesynctest W/Sync﹕ com.couchbase.lite.replicator.PullerInternal@40de4e98 no new remote revisions to fetch.  add lastInboxSequence (8) to pendingSequences (com.couchbase.lite.support.SequenceMap@40edb628)
09-18 03:40:57.063  32004-32029/com.samples.couchbaselitesynctest W/Sync﹕ com.couchbase.lite.replicator.PullerInternal@4138c188 no new remote revisions to fetch.  add lastInboxSequence (8) to pendingSequences (com.couchbase.lite.support.SequenceMap@413cee88)
09-18 03:41:00.013  32004-32004/com.samples.couchbaselitesynctest W/CBLite﹕ No pending attachment for digest: sha1-R+xgpKcyKLreDcbvdaC3IUn1DLw=
09-18 03:41:00.033  32004-32004/com.samples.couchbaselitesynctest E/Foo﹕ Error accessing CBL
    com.couchbase.lite.CouchbaseLiteException
            at com.couchbase.lite.Database.installAttachment(Database.java:2948)
            at com.couchbase.lite.Database.getAttachmentsFromRevision(Database.java:4285)
            at com.couchbase.lite.Database.putRevision(Database.java:4063)
            at com.couchbase.lite.Database.putRevision(Database.java:3923)
            at com.couchbase.lite.Document.putProperties(Document.java:420)
            at com.couchbase.lite.Document.putProperties(Document.java:252)
            at com.samples.couchbaselitesynctest.Note.toggleBookmark(Note.java:130)
            at com.samples.couchbaselitesynctest.MainActivity.onButtonClick(MainActivity.java:43)
            at java.lang.reflect.Method.invokeNative(Native Method)
            at java.lang.reflect.Method.invoke(Method.java:511)
            at android.view.View$1.onClick(View.java:3594)
            at android.view.View.performClick(View.java:4204)
            at android.view.View$PerformClick.run(View.java:17355)
            at android.os.Handler.handleCallback(Handler.java:725)
            at android.os.Handler.dispatchMessage(Handler.java:92)
            at android.os.Looper.loop(Looper.java:137)
            at android.app.ActivityThread.main(ActivityThread.java:5074)
            at java.lang.reflect.Method.invokeNative(Native Method)
            at java.lang.reflect.Method.invoke(Method.java:511)
            at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:793)
            at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:560)
            at dalvik.system.NativeStart.main(Native Method)
09-18 03:41:01.613   32004-1075/com.samples.couchbaselitesynctest E/RemoteRequest﹕ Got error status: 409 for http://10.0.0.72:4984/foo_bar/_local/d471c5625a24330f7d64f18421732ce328d05c50.  Reason: Conflict
09-18 03:41:01.613   32004-1075/com.samples.couchbaselitesynctest W/Sync﹕ com.couchbase.lite.replicator.ReplicationInternal$7@4146fdd8: Unable to save remote checkpoint
09-18 03:41:02.213   32004-1040/com.samples.couchbaselitesynctest E/RemoteRequest﹕ Got error status: 409 for http://10.0.0.72:4984/foo_bar/_local/877f5d3502fcd372604b88dcbd2d8291e5a68cb0.  Reason: Conflict
09-18 03:41:02.213   32004-1040/com.samples.couchbaselitesynctest W/Sync﹕ com.couchbase.lite.replicator.ReplicationInternal$7@414acb20: Unable to save remote checkpoint
```

