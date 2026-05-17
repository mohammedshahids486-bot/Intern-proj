package com.paryavarankavalu.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.paryavarankavalu.data.model.EcoLevel;
import com.paryavarankavalu.data.model.UserProfile;
import com.paryavarankavalu.data.model.UserRole;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserProfileDao_Impl implements UserProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserProfile> __insertionAdapterOfUserProfile;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<UserProfile> __updateAdapterOfUserProfile;

  private final SharedSQLiteStatement __preparedStmtOfAddReport;

  private final SharedSQLiteStatement __preparedStmtOfIncrementCleaned;

  public UserProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserProfile = new EntityInsertionAdapter<UserProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_profile` (`id`,`name`,`role`,`totalReports`,`cleanedReports`,`ecoKarmaPoints`,`level`,`joinDate`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProfile entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, __UserRole_enumToString(entity.getRole()));
        statement.bindLong(4, entity.getTotalReports());
        statement.bindLong(5, entity.getCleanedReports());
        statement.bindLong(6, entity.getEcoKarmaPoints());
        final String _tmp = __converters.fromEcoLevel(entity.getLevel());
        statement.bindString(7, _tmp);
        statement.bindLong(8, entity.getJoinDate());
      }
    };
    this.__updateAdapterOfUserProfile = new EntityDeletionOrUpdateAdapter<UserProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `user_profile` SET `id` = ?,`name` = ?,`role` = ?,`totalReports` = ?,`cleanedReports` = ?,`ecoKarmaPoints` = ?,`level` = ?,`joinDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProfile entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, __UserRole_enumToString(entity.getRole()));
        statement.bindLong(4, entity.getTotalReports());
        statement.bindLong(5, entity.getCleanedReports());
        statement.bindLong(6, entity.getEcoKarmaPoints());
        final String _tmp = __converters.fromEcoLevel(entity.getLevel());
        statement.bindString(7, _tmp);
        statement.bindLong(8, entity.getJoinDate());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfAddReport = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET ecoKarmaPoints = ecoKarmaPoints + ?, totalReports = totalReports + 1 WHERE id = 1";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementCleaned = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_profile SET cleanedReports = cleanedReports + 1 WHERE id = 1";
        return _query;
      }
    };
  }

  @Override
  public Object insertProfile(final UserProfile profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserProfile.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProfile(final UserProfile profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserProfile.handle(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addReport(final int points, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddReport.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, points);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfAddReport.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementCleaned(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementCleaned.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementCleaned.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<UserProfile> getProfile() {
    final String _sql = "SELECT * FROM user_profile WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"user_profile"}, false, new Callable<UserProfile>() {
      @Override
      @Nullable
      public UserProfile call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfTotalReports = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReports");
          final int _cursorIndexOfCleanedReports = CursorUtil.getColumnIndexOrThrow(_cursor, "cleanedReports");
          final int _cursorIndexOfEcoKarmaPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "ecoKarmaPoints");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "joinDate");
          final UserProfile _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final UserRole _tmpRole;
            _tmpRole = __UserRole_stringToEnum(_cursor.getString(_cursorIndexOfRole));
            final int _tmpTotalReports;
            _tmpTotalReports = _cursor.getInt(_cursorIndexOfTotalReports);
            final int _tmpCleanedReports;
            _tmpCleanedReports = _cursor.getInt(_cursorIndexOfCleanedReports);
            final int _tmpEcoKarmaPoints;
            _tmpEcoKarmaPoints = _cursor.getInt(_cursorIndexOfEcoKarmaPoints);
            final EcoLevel _tmpLevel;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfLevel);
            _tmpLevel = __converters.toEcoLevel(_tmp);
            final long _tmpJoinDate;
            _tmpJoinDate = _cursor.getLong(_cursorIndexOfJoinDate);
            _result = new UserProfile(_tmpId,_tmpName,_tmpRole,_tmpTotalReports,_tmpCleanedReports,_tmpEcoKarmaPoints,_tmpLevel,_tmpJoinDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getProfileOnce(final Continuation<? super UserProfile> $completion) {
    final String _sql = "SELECT * FROM user_profile WHERE id = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserProfile>() {
      @Override
      @Nullable
      public UserProfile call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfTotalReports = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReports");
          final int _cursorIndexOfCleanedReports = CursorUtil.getColumnIndexOrThrow(_cursor, "cleanedReports");
          final int _cursorIndexOfEcoKarmaPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "ecoKarmaPoints");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "joinDate");
          final UserProfile _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final UserRole _tmpRole;
            _tmpRole = __UserRole_stringToEnum(_cursor.getString(_cursorIndexOfRole));
            final int _tmpTotalReports;
            _tmpTotalReports = _cursor.getInt(_cursorIndexOfTotalReports);
            final int _tmpCleanedReports;
            _tmpCleanedReports = _cursor.getInt(_cursorIndexOfCleanedReports);
            final int _tmpEcoKarmaPoints;
            _tmpEcoKarmaPoints = _cursor.getInt(_cursorIndexOfEcoKarmaPoints);
            final EcoLevel _tmpLevel;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfLevel);
            _tmpLevel = __converters.toEcoLevel(_tmp);
            final long _tmpJoinDate;
            _tmpJoinDate = _cursor.getLong(_cursorIndexOfJoinDate);
            _result = new UserProfile(_tmpId,_tmpName,_tmpRole,_tmpTotalReports,_tmpCleanedReports,_tmpEcoKarmaPoints,_tmpLevel,_tmpJoinDate);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __UserRole_enumToString(@NonNull final UserRole _value) {
    switch (_value) {
      case USER: return "USER";
      case ADMIN: return "ADMIN";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private UserRole __UserRole_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "USER": return UserRole.USER;
      case "ADMIN": return UserRole.ADMIN;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
