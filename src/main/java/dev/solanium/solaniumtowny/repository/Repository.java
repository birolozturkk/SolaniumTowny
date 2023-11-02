package dev.solanium.solaniumtowny.repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.spring.DaoFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import dev.solanium.solaniumtowny.DatabaseObject;
import dev.solanium.solaniumtowny.SortedList;
import lombok.Getter;

import java.lang.invoke.TypeDescriptor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Repository<T extends DatabaseObject, ID> {

    protected final Dao<T, ID> dao;

    @Getter
    protected final SortedList<T> entries;

    private final ConnectionSource connectionSource;

    public Repository(ConnectionSource connectionSource, Class<T> dataClass, Comparator<T> comparator) {
        this.connectionSource = connectionSource;
        this.entries = new SortedList<>(comparator);
        try {
            TableUtils.createTableIfNotExists(connectionSource, dataClass);
            this.dao = DaoManager.createDao(connectionSource, dataClass);
            this.dao.setAutoCommit(getDatabaseConnection(), false);
            this.entries.addAll(dao.queryForAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.entries.forEach(t -> t.setChanged(false));
        sort();
    }

    public void addEntry(T t) {
        entries.add(t);
    }

    public T create(T entry) {
        try {
            return dao.createIfNotExists(entry);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            List<T> entryList = new ArrayList<>(entries);
            for (T value : entryList) {
                if (!value.isChanged()) continue;
                dao.createOrUpdate(value);
                value.setChanged(false);
            }
            dao.commit(getDatabaseConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected void sort() {
        entries.sort();
    }

    private DatabaseConnection getDatabaseConnection() {
        try {
            return connectionSource.getReadWriteConnection(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
