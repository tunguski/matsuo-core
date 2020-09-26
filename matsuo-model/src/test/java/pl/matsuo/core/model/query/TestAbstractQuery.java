package pl.matsuo.core.model.query;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pl.matsuo.core.model.query.condition.Condition;
import pl.matsuo.core.model.query.condition.FromPart;
import pl.matsuo.core.model.query.condition.SelectPart;

public class TestAbstractQuery {

  @Test
  public void testSelect() {
    AbstractQuery query = new AbstractQuery(TheModel.class).select("sth");
    assertEquals(
        "SELECT sth FROM pl.matsuo.core.model.query.TheModel theModel", query.printQuery());
  }

  @Test
  public void testCondition() {
    AbstractQuery query =
        new AbstractQuery(TheModel.class)
            .condition(
                new Condition() {
                  @Override
                  public String print(AbstractQuery query) {
                    return "test_condition";
                  }
                });
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel WHERE test_condition",
        query.printQuery());
  }

  @Test
  public void testParts() {
    AbstractQuery query =
        new AbstractQuery(TheModel.class)
            .parts(
                new Condition() {
                  @Override
                  public String print(AbstractQuery query) {
                    return "test_condition";
                  }
                },
                new FromPart("testJoin", "alias", "joinPath"),
                new SelectPart("test_select"));
    assertEquals(
        "SELECT test_select FROM pl.matsuo.core.model.query.TheModel theModel testJoin joinPath alias WHERE test_condition",
        query.printQuery());
  }

  @Test
  public void testGroupBy() {
    AbstractQuery query = new AbstractQuery(TheModel.class).groupBy("test");
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel GROUP BY test", query.printQuery());
  }

  @Test
  public void testHaving() {
    AbstractQuery query = new AbstractQuery(TheModel.class).having(query1 -> "having");
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel HAVING having", query.printQuery());
  }

  @Test
  public void testOrderBy() {
    AbstractQuery query = new AbstractQuery(TheModel.class).orderBy("having");
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel ORDER BY having", query.printQuery());
  }

  @Test
  public void testLimit() {
    AbstractQuery query = new AbstractQuery(TheModel.class).limit(99);
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel", query.printQuery());
  }

  //  @Test
  //  public void testOffset() {
  //    AbstractQuery query = new AbstractQuery(TheModel.class).offset(77);
  //    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel ",
  //        query.printQuery());
  //  }

  @Test
  public void testInitializer() {
    AbstractQuery query = new AbstractQuery(TheModel.class).initializer(element -> {});
    assertEquals(1, query.initializers.size());
  }

  //  @Test
  //  public void testQuery() {
  //    AbstractQuery query = new AbstractQuery(TheModel.class).select("sth");
  //
  //    query.sessionFactory = mock(SessionFactory.class);
  //
  //    query.query(7);
  //  }
}
