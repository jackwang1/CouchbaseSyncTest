package com.samples.couchbaselitesynctest;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Note {

    private final static String TAG = Note.class.getSimpleName();

    private final static String ALL_VIEW = "notes";
    private final static String BOOKMARK_VIEW = "bookmarks";
    private final static String ATTACHMENT_VIEW = "attachments";
    private final static String DOC_TYPE = "note";

    public static Query getQuery(Database database, String caseId) {
        View view = database.getView(ALL_VIEW);
        if (view.getMap() == null) {
            Mapper mapper = new Mapper() {
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get("type"))) {
                        List keys = Arrays.asList(document.get("case_id"), document.get("created"));
                        emitter.emit(keys, document);
                    }
                }
            };
            view.setMap(mapper, "1");
        }

        Query query = view.createQuery();
        query.setStartKey(Arrays.asList(caseId));
        query.setEndKey(Arrays.asList(caseId, new HashMap<String, Object>()));

        return query;
    }

    public static Query getBookmarkedQuery(Database database, String caseId) {
        View view = database.getView(BOOKMARK_VIEW);
        if (view.getMap() == null) {
            Mapper mapper = new Mapper() {
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get("type")) && isBookmarked(document)) {
                        List keys = Arrays.asList(document.get("case_id"), document.get("created"));
                        emitter.emit(keys, document);
                    }
                }
            };
            view.setMap(mapper, "1");
        }

        Query query = view.createQuery();
        query.setStartKey(Arrays.asList(caseId));
        query.setEndKey(Arrays.asList(caseId, new HashMap<String, Object>()));

        return query;
    }

    public static Query getAttachmentQuery(Database database, String caseId, String path) {
        View view = database.getView(ATTACHMENT_VIEW);
        if (view.getMap() == null) {
            Mapper mapper = new Mapper() {
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get("type"))) {
                        List keys = Arrays.asList(document.get("case_id"), document.get("file_name"));
                        emitter.emit(keys, document);
                    }
                }
            };
            view.setMap(mapper, "1");
        }

        Query query = view.createQuery();
        query.setStartKey(Arrays.asList(caseId, path));
        query.setEndKey(Arrays.asList(caseId, path));

        return query;
    }

    public static Document createNote(Database database, String caseId) throws CouchbaseLiteException {
        Map<String, Object> properties = new HashMap();
        properties.put("type", DOC_TYPE);
        properties.put("case_id", caseId);
        properties.put("created", System.currentTimeMillis());
        Document doc = database.createDocument();
        doc.putProperties(properties);
        return doc;
    }

    public static void addAttachment(Document note, String type, File file)
            throws CouchbaseLiteException, FileNotFoundException {
        UnsavedRevision rev = note.createRevision();
        Map<String, Object> properties = rev.getProperties();
        properties.put("file_name", file.getAbsolutePath());
        rev.setProperties(properties);
        rev.setAttachment("attachment", type, new FileInputStream(file));
        rev.save();
    }

    public static void addAttachment(Document note, String type, InputStream is)
            throws CouchbaseLiteException {
        UnsavedRevision rev = note.createRevision();
        rev.setAttachment("attachment", type, is);
        rev.save();
    }

    public static void deleteAttachment(Document note, File file) throws CouchbaseLiteException {
        if (note != null && file.getAbsolutePath().equals(note.getProperty("file_name"))) {
            note.delete();
        }
    }

    public static boolean toggleBookmark(Document note) throws CouchbaseLiteException {
        Map<String, Object> properties = new HashMap<>(note.getProperties());
        boolean bookmarked = !isBookmarked(properties);
        properties.put("bookmark", bookmarked);
        note.putProperties(properties);
        return bookmarked;
    }

    public static boolean isBookmarked(Document note) throws CouchbaseLiteException {
        return isBookmarked(note.getProperties());
    }

    public static boolean isBookmarked(Map<String, Object> properties) {
        return properties.containsKey("bookmark") ? (boolean)properties.get("bookmark") : false;
    }
}
