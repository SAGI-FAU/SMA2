package com.sma2.sma2.DataAccess;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import android.net.Uri;
import com.sma2.sma2.DataAccess.Exercise.FragmentConverter;
import com.sma2.sma2.DataAccess.Exercise.UriConverter;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EXERCISE".
*/
public class ExerciseDao extends AbstractDao<Exercise, Long> {

    public static final String TABLENAME = "EXERCISE";

    /**
     * Properties of entity Exercise.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property ExerciseType = new Property(2, String.class, "exerciseType", false, "EXERCISE_TYPE");
        public final static Property ShortDescription = new Property(3, String.class, "shortDescription", false, "SHORT_DESCRIPTION");
        public final static Property ShortInstructions = new Property(4, String.class, "shortInstructions", false, "SHORT_INSTRUCTIONS");
        public final static Property InstructionVideoPath = new Property(5, String.class, "instructionVideoPath", false, "INSTRUCTION_VIDEO_PATH");
        public final static Property InstructionTextPath = new Property(6, String.class, "instructionTextPath", false, "INSTRUCTION_TEXT_PATH");
        public final static Property FragmentClass = new Property(7, String.class, "fragmentClass", false, "FRAGMENT_CLASS");
    }

    private final UriConverter instructionVideoPathConverter = new UriConverter();
    private final UriConverter instructionTextPathConverter = new UriConverter();
    private final FragmentConverter fragmentClassConverter = new FragmentConverter();

    public ExerciseDao(DaoConfig config) {
        super(config);
    }
    
    public ExerciseDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EXERCISE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"EXERCISE_TYPE\" TEXT NOT NULL ," + // 2: exerciseType
                "\"SHORT_DESCRIPTION\" TEXT," + // 3: shortDescription
                "\"SHORT_INSTRUCTIONS\" TEXT," + // 4: shortInstructions
                "\"INSTRUCTION_VIDEO_PATH\" TEXT," + // 5: instructionVideoPath
                "\"INSTRUCTION_TEXT_PATH\" TEXT," + // 6: instructionTextPath
                "\"FRAGMENT_CLASS\" TEXT);"); // 7: fragmentClass
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EXERCISE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Exercise entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getExerciseType());
 
        String shortDescription = entity.getShortDescription();
        if (shortDescription != null) {
            stmt.bindString(4, shortDescription);
        }
 
        String shortInstructions = entity.getShortInstructions();
        if (shortInstructions != null) {
            stmt.bindString(5, shortInstructions);
        }
 
        Uri instructionVideoPath = entity.getInstructionVideoPath();
        if (instructionVideoPath != null) {
            stmt.bindString(6, instructionVideoPathConverter.convertToDatabaseValue(instructionVideoPath));
        }
 
        Uri instructionTextPath = entity.getInstructionTextPath();
        if (instructionTextPath != null) {
            stmt.bindString(7, instructionTextPathConverter.convertToDatabaseValue(instructionTextPath));
        }
 
        Class fragmentClass = entity.getFragmentClass();
        if (fragmentClass != null) {
            stmt.bindString(8, fragmentClassConverter.convertToDatabaseValue(fragmentClass));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Exercise entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getExerciseType());
 
        String shortDescription = entity.getShortDescription();
        if (shortDescription != null) {
            stmt.bindString(4, shortDescription);
        }
 
        String shortInstructions = entity.getShortInstructions();
        if (shortInstructions != null) {
            stmt.bindString(5, shortInstructions);
        }
 
        Uri instructionVideoPath = entity.getInstructionVideoPath();
        if (instructionVideoPath != null) {
            stmt.bindString(6, instructionVideoPathConverter.convertToDatabaseValue(instructionVideoPath));
        }
 
        Uri instructionTextPath = entity.getInstructionTextPath();
        if (instructionTextPath != null) {
            stmt.bindString(7, instructionTextPathConverter.convertToDatabaseValue(instructionTextPath));
        }
 
        Class fragmentClass = entity.getFragmentClass();
        if (fragmentClass != null) {
            stmt.bindString(8, fragmentClassConverter.convertToDatabaseValue(fragmentClass));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Exercise readEntity(Cursor cursor, int offset) {
        Exercise entity = new Exercise( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.getString(offset + 2), // exerciseType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // shortDescription
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // shortInstructions
            cursor.isNull(offset + 5) ? null : instructionVideoPathConverter.convertToEntityProperty(cursor.getString(offset + 5)), // instructionVideoPath
            cursor.isNull(offset + 6) ? null : instructionTextPathConverter.convertToEntityProperty(cursor.getString(offset + 6)), // instructionTextPath
            cursor.isNull(offset + 7) ? null : fragmentClassConverter.convertToEntityProperty(cursor.getString(offset + 7)) // fragmentClass
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Exercise entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setExerciseType(cursor.getString(offset + 2));
        entity.setShortDescription(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setShortInstructions(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setInstructionVideoPath(cursor.isNull(offset + 5) ? null : instructionVideoPathConverter.convertToEntityProperty(cursor.getString(offset + 5)));
        entity.setInstructionTextPath(cursor.isNull(offset + 6) ? null : instructionTextPathConverter.convertToEntityProperty(cursor.getString(offset + 6)));
        entity.setFragmentClass(cursor.isNull(offset + 7) ? null : fragmentClassConverter.convertToEntityProperty(cursor.getString(offset + 7)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Exercise entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Exercise entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Exercise entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}