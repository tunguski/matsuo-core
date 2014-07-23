package pl.matsuo.core.model.query;

import org.junit.Test;
import pl.matsuo.core.model.query.condition.QueryPart;

import static org.junit.Assert.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 26.09.13.
 */
public class TestQueryBuilder {


  private static final String queryBase = "FROM pl.matsuo.core.model.query.TheModel theModel";


  protected String doQuery(QueryPart ... queryParts) {
    return query(TheModel.class, queryParts).printQuery();
  }


  @Test
  public void testBasic() {
    // podstawowe zapytani
    assertEquals(queryBase, doQuery());
  }


  @Test
  public void testSelect() {
    // klauzula select
    assertEquals("SELECT theModel.id, theModel " + queryBase,
        doQuery(select("theModel.id, theModel")));
  }


  @Test
  public void testWhere() {
    // klauzula where
    assertEquals(queryBase + " WHERE id = 12",
        doQuery(eq("id", 12)));
  }


  @Test
  public void testAnd() {
    // warunek and
    assertEquals(queryBase + " WHERE (id = 12 AND id < 200)",
        doQuery(and(eq("id", 12), lt("id", 200))));
  }


  @Test
  public void testOrderBy() {
    // klauzula order by
    assertEquals(queryBase + " ORDER BY id DESC",
        doQuery(orderBy("id DESC")));
  }


  @Test
  public void testJoin() {
    // theta join
    assertEquals(queryBase + " , pl.matsuo.core.model.query.TheModel appointment WHERE (appointment.id = id)",
        doQuery(leftJoin("appointment", TheModel.class, cond("appointment.id = id"))));
  }


  @Test
  public void testLimit() {
    // limit
    assertEquals(queryBase + " LIMIT 20", doQuery(limit(20)));
  }


  @Test
  public void testOffset() {
    // offset
    assertEquals(queryBase + " OFFSET 20", doQuery(offset(20)));
  }
}
