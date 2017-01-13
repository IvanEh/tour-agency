package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.util.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.stream.*;

public class TourJdbcDao implements TourDao {

    private static final String CREATE_SQL =
            "INSERT INTO `tour` (`title`, `description`, `type`," +
                    " `hot`, `price`, `enabled`) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String FIND_ALL_SQL = "SELECT * FROM tour";

    private static final String FIND_ALL_ORDERED_SQL =
            FIND_ALL_SQL + " ORDER BY hot DESC, id DESC";

    private static final String READ_SQL = "SELECT * FROM tour WHERE id=?";

    private static final String UPDATE_SQL = "UPDATE `tour` SET `title`=?, " +
            "`description`=?, `type`=?, `hot`=?, `price`=?, `enabled`=? WHERE `id`=?";

    private static final String DELETE_SQL = "DELETE FROM tour WHERE id=?";

    private static final String COMPUTE_PRICE_SQL =
            "SELECT tour.price * cast((100 - discount)/100 as DECIMAL(10,2)) " +
            "FROM  `user`, tour WHERE `user`.id =? AND tour.id=?";

    private static final String RANDOM_HOT_SQL = "SELECT * FROM tour " +
            " WHERE id >= FLOOR(RAND()*(SELECT MAX(id) FROM tour)) " +
            " AND `hot`='1' LIMIT 1 ";

    private static final BigDecimal LARGE_DECIMAL = new BigDecimal(Long.MAX_VALUE);

    private ConnectionManager connectionManager;

    private JdbcTemplate jdbcTemplate;

    public TourJdbcDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.jdbcTemplate = new JdbcTemplate(connectionManager);
    }

    @Override
    public Long create(Tour t) {
        return jdbcTemplate.insert(CREATE_SQL, t.getTitle(), t.getDescription(), t.getType().ordinal(),
                t.isHot(), t.getPrice(), t.isEnabled());
    }

    @Override
    public Tour read(Long id) {
        return jdbcTemplate.queryObject(TourJdbcDao::fromResultSet, READ_SQL, id);
    }

    @Override
    public void update(Tour t) {
        jdbcTemplate.update(UPDATE_SQL, t.getTitle(), t.getDescription(),
                t.getType().ordinal(), t.isHot(), t.getPrice(), t.isEnabled(), t.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public List<Tour> findAll() {
        return jdbcTemplate.queryObjects(TourJdbcDao::fromResultSet, FIND_ALL_ORDERED_SQL);
    }

    @Override
    public BigDecimal computePrice(Long tourId, Long userId) {
        return jdbcTemplate.queryObjects((rs) -> rs.getBigDecimal(1), COMPUTE_PRICE_SQL, userId, tourId).get(0);
    }

    @Override
    public Tour findRandomHot() {
        return jdbcTemplate.queryObject(TourJdbcDao::fromResultSet, RANDOM_HOT_SQL);
    }

    @Override
    public Slice<Tour> getToursSliceByCriteria(int count, Tour anchor, ScrollDirection dir,
                                               Ordering priceOrdering, TourType... types) {
        List<String> typesList = Arrays.stream(types)
                .map(TourType::ordinal)
                .map(String::valueOf)
                .collect(Collectors.toList());

        if(anchor == null) {
            BigDecimal priceAnchor = priceOrdering == Ordering.ASC ? BigDecimal.ZERO : LARGE_DECIMAL;
            anchor = new Tour(Long.MAX_VALUE);
            anchor.setPrice(priceAnchor);
        }

        WhereBuilder whereBuilder = QueryBuilder.select("tour").where("type", WhereBuilder.inCond(typesList))
                .and("enabled", "=TRUE");

        if (dir == ScrollDirection.DOWN) {
            whereBuilder = whereBuilder.and("id", ScrollDirection.DOWN.getRel() + anchor.getId());
        } else {
            whereBuilder = whereBuilder.and("id", ScrollDirection.UP.getRel() + anchor.getId());
        }

        String rel;
        switch (priceOrdering) {
            case ASC:
                rel = dir == ScrollDirection.DOWN ? ">" : "<";
                whereBuilder = whereBuilder.and("price", rel + anchor.getPrice());
                break;
            case DESC:
                rel = dir == ScrollDirection.DOWN ? "<" : ">";
                whereBuilder = whereBuilder.and("price", rel + anchor.getPrice());
                break;
        }

        String query = whereBuilder.orderBy("price", priceOrdering)
                        .and("id", Ordering.DESC)
                        .limit(count)
                        .build();

        List<Tour> tours = jdbcTemplate.queryObjects(TourJdbcDao::fromResultSet, query);

        Tour first = null;
        Tour last = null;

        if(!tours.isEmpty()) {
            first = tours.get(0);
        }

        if(tours.size() == count) {
            last = tours.get(tours.size() - 1);
        }

        return new Slice<Tour>(tours, first, last);
    }

    private static Tour fromResultSet(ResultSet rs) throws SQLException {
        return TourMapper.map(rs);
    }
}
