package com.sma2.apkinson.DataAccess;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.sma2.apkinson.ExerciseFragments.ExerciseFragment;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Exercise implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String exerciseType;
    @Property
    private String shortDescription;
    @Property
    private String shortInstructions;
    @Convert(converter = UriConverter.class, columnType = String.class)
    private Uri instructionVideoPath;
    @Convert(converter = UriConverter.class, columnType = String.class)
    private Uri instructionTextPath;
    @Convert(converter = FragmentConverter.class, columnType = String.class)
    private Class<? extends ExerciseFragment> fragmentClass;

    public static class UriConverter implements PropertyConverter<Uri, String> {
        @Override
        public Uri convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return Uri.parse(databaseValue);
        }
        @Override
        public String convertToDatabaseValue(Uri entityProperty) {
            return entityProperty == null ? null : entityProperty.toString();
        }
    }

    public static class FragmentConverter implements PropertyConverter<Class<? extends ExerciseFragment>, String> {
        @Override
        public Class<? extends ExerciseFragment> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return Exercise.getFragmentFromString(databaseValue);
        }
        @Override
        public String convertToDatabaseValue(Class<? extends ExerciseFragment> entityProperty) {
            return entityProperty == null ? null : Exercise.getStringFromFragment(entityProperty);
        }
    }

    @Keep
    public Exercise(long id,
                    String name,
                    String exerciseType,
                    String shortDescription,
                    String shortInstructions,
                    Uri instructionVideoPath,
                    Uri instructionTextPath,
                    Class<? extends ExerciseFragment> fragmentClass){
        super();
        this.id = id;
        this.name = name;
        this.exerciseType = exerciseType;
        this.shortDescription = shortDescription;
        this.shortInstructions = shortInstructions;
        this.instructionVideoPath = instructionVideoPath;
        this.instructionTextPath = instructionTextPath;
        this.fragmentClass = fragmentClass;
    }

    @Keep
    public Exercise(long id,
                    String name,
                    String exerciseType,
                    String shortDescription,
                    String shortInstructions,
                    Uri instructionVideoPath,
                    Uri instructionTextPath,
                    String fragmentClassString){
        this(
                id,
                name,
                exerciseType,
                shortDescription,
                shortInstructions,
                instructionVideoPath,
                instructionTextPath,
                getFragmentFromString(fragmentClassString));
    }

    @Keep
    public Exercise(Parcel serializedExercise) {
        super();
        this.id = serializedExercise.readLong();
        this.name = serializedExercise.readString();
        this.exerciseType = serializedExercise.readString();
        this.shortDescription = serializedExercise.readString();
        this.shortInstructions = serializedExercise.readString();
        this.instructionVideoPath = Uri.parse(serializedExercise.readString());
        this.instructionTextPath = Uri.parse(serializedExercise.readString());
        this.fragmentClass = getFragmentFromString(serializedExercise.readString());

    }
    @Generated(hash = 2073761405)
    public Exercise(Long id, @NotNull String name, @NotNull String exerciseType, String shortDescription,
            String shortInstructions, Uri instructionVideoPath, Uri instructionTextPath,
            Class<? extends ExerciseFragment> fragmentClass) {
        this.id = id;
        this.name = name;
        this.exerciseType = exerciseType;
        this.shortDescription = shortDescription;
        this.shortInstructions = shortInstructions;
        this.instructionVideoPath = instructionVideoPath;
        this.instructionTextPath = instructionTextPath;
        this.fragmentClass = fragmentClass;
    }
    @Generated(hash = 1537691247)
    public Exercise() {
    }

    static private Class<? extends ExerciseFragment> getFragmentFromString(String fragmentClassString) {
        Class<? extends ExerciseFragment> fragmentClass;
        try {
            fragmentClass = Class.forName(fragmentClassString).asSubclass(ExerciseFragment.class);
        } catch (Exception e) {
            throw new RuntimeException("The specified Fragment class for this exercise is invalid.");
        }
        return fragmentClass;
    }

    static private String getStringFromFragment(Class<? extends ExerciseFragment> fragmentClass) {
        return fragmentClass.getName();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Exercise createFromParcel(Parcel serializedExercise) {
            return new Exercise(serializedExercise);
        }

        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Uri getInstructionVideoPath() {
        return instructionVideoPath;
    }

    public Class<? extends ExerciseFragment> getFragmentClass() {
        return fragmentClass;
    }

    public Uri getInstructionTextPath() {
        return instructionTextPath;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getShortInstructions() {
        return shortInstructions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.exerciseType);
        parcel.writeString(this.shortDescription);
        parcel.writeString(this.shortInstructions);
        parcel.writeString(this.instructionTextPath.toString());
        parcel.writeString(this.instructionVideoPath.toString());
        parcel.writeString(getStringFromFragment(this.fragmentClass));
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    public void setShortInstructions(String shortInstructions) {
        this.shortInstructions = shortInstructions;
    }
    public void setInstructionVideoPath(Uri instructionVideoPath) {
        this.instructionVideoPath = instructionVideoPath;
    }
    public void setInstructionTextPath(Uri instructionTextPath) {
        this.instructionTextPath = instructionTextPath;
    }
    public void setFragmentClass(Class<? extends ExerciseFragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }
}
