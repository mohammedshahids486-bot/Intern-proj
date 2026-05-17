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
import com.paryavarankavalu.data.model.ReportStatus;
import com.paryavarankavalu.data.model.WasteReport;
import com.paryavarankavalu.data.model.WasteType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WasteReportDao_Impl implements WasteReportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WasteReport> __insertionAdapterOfWasteReport;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<WasteReport> __deletionAdapterOfWasteReport;

  private final EntityDeletionOrUpdateAdapter<WasteReport> __updateAdapterOfWasteReport;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  public WasteReportDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWasteReport = new EntityInsertionAdapter<WasteReport>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `waste_reports` (`id`,`title`,`description`,`wasteType`,`latitude`,`longitude`,`address`,`photoPath`,`status`,`timestamp`,`reporterName`,`ecoKarmaPoints`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WasteReport entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        final String _tmp = __converters.fromWasteType(entity.getWasteType());
        statement.bindString(4, _tmp);
        statement.bindDouble(5, entity.getLatitude());
        statement.bindDouble(6, entity.getLongitude());
        statement.bindString(7, entity.getAddress());
        statement.bindString(8, entity.getPhotoPath());
        final String _tmp_1 = __converters.fromReportStatus(entity.getStatus());
        statement.bindString(9, _tmp_1);
        statement.bindLong(10, entity.getTimestamp());
        statement.bindString(11, entity.getReporterName());
        statement.bindLong(12, entity.getEcoKarmaPoints());
      }
    };
    this.__deletionAdapterOfWasteReport = new EntityDeletionOrUpdateAdapter<WasteReport>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `waste_reports` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WasteReport entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfWasteReport = new EntityDeletionOrUpdateAdapter<WasteReport>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `waste_reports` SET `id` = ?,`title` = ?,`description` = ?,`wasteType` = ?,`latitude` = ?,`longitude` = ?,`address` = ?,`photoPath` = ?,`status` = ?,`timestamp` = ?,`reporterName` = ?,`ecoKarmaPoints` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WasteReport entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        final String _tmp = __converters.fromWasteType(entity.getWasteType());
        statement.bindString(4, _tmp);
        statement.bindDouble(5, entity.getLatitude());
        statement.bindDouble(6, entity.getLongitude());
        statement.bindString(7, entity.getAddress());
        statement.bindString(8, entity.getPhotoPath());
        final String _tmp_1 = __converters.fromReportStatus(entity.getStatus());
        statement.bindString(9, _tmp_1);
        statement.bindLong(10, entity.getTimestamp());
        statement.bindString(11, entity.getReporterName());
        statement.bindLong(12, entity.getEcoKarmaPoints());
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE waste_reports SET status = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertReport(final WasteReport report,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWasteReport.insertAndReturnId(report);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteReport(final WasteReport report,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfWasteReport.handle(report);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReport(final WasteReport report,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfWasteReport.handle(report);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final long id, final ReportStatus status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromReportStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<WasteReport>> getAllReports() {
    final String _sql = "SELECT * FROM waste_reports ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"waste_reports"}, false, new Callable<List<WasteReport>>() {
      @Override
      @Nullable
      public List<WasteReport> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWasteType = CursorUtil.getColumnIndexOrThrow(_cursor, "wasteType");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfReporterName = CursorUtil.getColumnIndexOrThrow(_cursor, "reporterName");
          final int _cursorIndexOfEcoKarmaPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "ecoKarmaPoints");
          final List<WasteReport> _result = new ArrayList<WasteReport>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WasteReport _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final WasteType _tmpWasteType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfWasteType);
            _tmpWasteType = __converters.toWasteType(_tmp);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpAddress;
            _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            final String _tmpPhotoPath;
            _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            final ReportStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toReportStatus(_tmp_1);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpReporterName;
            _tmpReporterName = _cursor.getString(_cursorIndexOfReporterName);
            final int _tmpEcoKarmaPoints;
            _tmpEcoKarmaPoints = _cursor.getInt(_cursorIndexOfEcoKarmaPoints);
            _item = new WasteReport(_tmpId,_tmpTitle,_tmpDescription,_tmpWasteType,_tmpLatitude,_tmpLongitude,_tmpAddress,_tmpPhotoPath,_tmpStatus,_tmpTimestamp,_tmpReporterName,_tmpEcoKarmaPoints);
            _result.add(_item);
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
  public LiveData<List<WasteReport>> getReportsByStatus(final ReportStatus status) {
    final String _sql = "SELECT * FROM waste_reports WHERE status = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromReportStatus(status);
    _statement.bindString(_argIndex, _tmp);
    return __db.getInvalidationTracker().createLiveData(new String[] {"waste_reports"}, false, new Callable<List<WasteReport>>() {
      @Override
      @Nullable
      public List<WasteReport> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWasteType = CursorUtil.getColumnIndexOrThrow(_cursor, "wasteType");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfReporterName = CursorUtil.getColumnIndexOrThrow(_cursor, "reporterName");
          final int _cursorIndexOfEcoKarmaPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "ecoKarmaPoints");
          final List<WasteReport> _result = new ArrayList<WasteReport>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WasteReport _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final WasteType _tmpWasteType;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfWasteType);
            _tmpWasteType = __converters.toWasteType(_tmp_1);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpAddress;
            _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            final String _tmpPhotoPath;
            _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            final ReportStatus _tmpStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toReportStatus(_tmp_2);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpReporterName;
            _tmpReporterName = _cursor.getString(_cursorIndexOfReporterName);
            final int _tmpEcoKarmaPoints;
            _tmpEcoKarmaPoints = _cursor.getInt(_cursorIndexOfEcoKarmaPoints);
            _item = new WasteReport(_tmpId,_tmpTitle,_tmpDescription,_tmpWasteType,_tmpLatitude,_tmpLongitude,_tmpAddress,_tmpPhotoPath,_tmpStatus,_tmpTimestamp,_tmpReporterName,_tmpEcoKarmaPoints);
            _result.add(_item);
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
  public Object getReportById(final long id, final Continuation<? super WasteReport> $completion) {
    final String _sql = "SELECT * FROM waste_reports WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WasteReport>() {
      @Override
      @Nullable
      public WasteReport call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWasteType = CursorUtil.getColumnIndexOrThrow(_cursor, "wasteType");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfReporterName = CursorUtil.getColumnIndexOrThrow(_cursor, "reporterName");
          final int _cursorIndexOfEcoKarmaPoints = CursorUtil.getColumnIndexOrThrow(_cursor, "ecoKarmaPoints");
          final WasteReport _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final WasteType _tmpWasteType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfWasteType);
            _tmpWasteType = __converters.toWasteType(_tmp);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final String _tmpAddress;
            _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            final String _tmpPhotoPath;
            _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            final ReportStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toReportStatus(_tmp_1);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpReporterName;
            _tmpReporterName = _cursor.getString(_cursorIndexOfReporterName);
            final int _tmpEcoKarmaPoints;
            _tmpEcoKarmaPoints = _cursor.getInt(_cursorIndexOfEcoKarmaPoints);
            _result = new WasteReport(_tmpId,_tmpTitle,_tmpDescription,_tmpWasteType,_tmpLatitude,_tmpLongitude,_tmpAddress,_tmpPhotoPath,_tmpStatus,_tmpTimestamp,_tmpReporterName,_tmpEcoKarmaPoints);
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

  @Override
  public Object getTotalReports(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM waste_reports";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCleanedReports(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM waste_reports WHERE status = 'CLEANED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalKarmaPoints(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT SUM(ecoKarmaPoints) FROM waste_reports WHERE status = 'CLEANED' OR status = 'VERIFIED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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
}
