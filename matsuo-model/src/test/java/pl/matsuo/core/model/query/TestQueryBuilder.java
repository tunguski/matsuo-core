package pl.matsuo.core.model.query;

import org.junit.Test;

import static org.junit.Assert.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 26.09.13.
 */
public class TestQueryBuilder {


  @Test
  public void testQueries() {
    // podstawowe zapytani
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel", query(TheModel.class).printQuery());
    // klauzula select
    assertEquals("SELECT theModel.id, theModel FROM pl.matsuo.core.model.query.TheModel theModel",
                    query(TheModel.class, select("theModel.id, theModel")).printQuery());
    // klauzula where
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel WHERE id = 12",
                    query(TheModel.class, eq("id", 12)).printQuery());
    // warunek and
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel WHERE (id = 12 AND id < 200)",
                    query(TheModel.class, and(eq("id", 12), lt("id", 200))).printQuery());
    // klauzula order by
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel ORDER BY id DESC",
                    query(TheModel.class).orderBy("id DESC").printQuery());
    // theta join
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel , " +
                     "pl.matsuo.core.model.query.TheModel appointment WHERE (appointment.id = id)",
                    query(TheModel.class, leftJoin("appointment", TheModel.class, cond("appointment.id = id"))).printQuery());
  }
}
