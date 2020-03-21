package pl.matsuo.core.model.query;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;

import org.hibernate.criterion.MatchMode;
import org.junit.Test;

public class TestQueryBuilder {

  AbstractQuery abstractQuery = new AbstractQuery(TheModel.class);

  @Test
  public void testQueries() {
    // klauzula where
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel WHERE id = 12",
        query(TheModel.class, eq(TheModel::getId, 12)).printQuery());
    // klauzula order by
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel ORDER BY id DESC",
        query(TheModel.class).orderBy("id DESC").printQuery());
  }

  @Test
  public void testQuery() throws Exception {
    // podstawowe zapytani
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel", query(TheModel.class).printQuery());
  }

  @Test
  public void testMaybe() throws Exception {
    assertEquals("test = '1'", maybe(true, eq(TheModel::getTest, "1")).print(abstractQuery));
    assertNull(maybe(false, eq(TheModel::getTest, "1")));
  }

  @Test
  public void testMaybeEq() throws Exception {
    assertEquals("test = '1'", maybeEq("1", TheModel::getTest).print(abstractQuery));
    assertNull(maybeEq(null, TheModel::getTest));
  }

  @Test
  public void testMaybe1() throws Exception {
    assertEquals("test = '1'", maybe("1", eq(TheModel::getTest, "1")).print(abstractQuery));
    assertNull(maybe(null, eq(TheModel::getTest, null)));
  }

  @Test
  public void testSelect() throws Exception {
    // klauzula select
    assertEquals(
        "SELECT theModel.id, theModel FROM pl.matsuo.core.model.query.TheModel theModel",
        query(TheModel.class, select("theModel.id, theModel")).printQuery());
  }

  @Test
  public void testOperator() throws Exception {
    assertEquals("field %% 1", operator(TheModel::getField, "%%", 1).print(abstractQuery));
  }

  @Test
  public void testEq() throws Exception {
    assertEquals("field = 1", eq(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testNe() throws Exception {
    assertEquals("field != 1", ne(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testGt() throws Exception {
    assertEquals("field > 1", gt(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testGe() throws Exception {
    assertEquals("field >= 1", ge(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testLt() throws Exception {
    assertEquals("field < 1", lt(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testLe() throws Exception {
    assertEquals("field <= 1", le(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testIlike() throws Exception {
    assertEquals("lower(field) like '%1%'", ilike(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testIlike1() throws Exception {
    assertEquals(
        "lower(field) like '1%'",
        ilike(TheModel::getField, 1, MatchMode.START).print(abstractQuery));
    assertEquals(
        "lower(field) like '%1'", ilike(TheModel::getField, 1, MatchMode.END).print(abstractQuery));
  }

  @Test
  public void testIn() throws Exception {
    assertEquals(
        "field in (1, 2, 3)", in(TheModel::getField, asList(1, 2, 3)).print(abstractQuery));
  }

  @Test
  public void testIn1() throws Exception {
    assertEquals(
        "field in (1, 2, 3)", in(TheModel::getField, new Object[] {1, 2, 3}).print(abstractQuery));
  }

  @Test
  public void testIn2() throws Exception {
    assertEquals(
        "field in (FROM pl.matsuo.core.model.query.TheModel theModel WHERE id = 7)",
        in(TheModel::getField, query(TheModel.class, eq(TheModel::getId, 7))).print(abstractQuery));
  }

  @Test
  public void testBetween() throws Exception {
    assertEquals("field BETWEEN 1 AND 10", between(TheModel::getField, 1, 10).print(abstractQuery));
  }

  @Test
  public void testIsNull() throws Exception {
    assertEquals("field IS NULL", isNull(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testIsNotNull() throws Exception {
    assertEquals("field IS NOT NULL", isNotNull(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testEqOrIsNull() throws Exception {
    assertEquals(
        "(field = 1 OR field IS NULL)", eqOrIsNull(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testOr() throws Exception {
    assertEquals(
        "(f1 = 1 OR f2 = 2)",
        or(eq(TheModel::getF1, 1), eq(TheModel::getF2, 2)).print(abstractQuery));
  }

  @Test
  public void testAnd() throws Exception {
    // warunek and
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel WHERE (id = 12 AND id < 200)",
        query(TheModel.class, and(eq(TheModel::getId, 12), lt(TheModel::getId, 200))).printQuery());
  }

  @Test
  public void testNot() throws Exception {
    assertEquals("NOT field = 1", not(eq(TheModel::getField, 1)).print(abstractQuery));
  }

  @Test
  public void testCond() throws Exception {
    assertEquals("(test_condition)", cond("test_condition").print(abstractQuery));
  }

  @Test
  public void testMemberOf() throws Exception {
    assertEquals("field MEMBER OF 7", memberOf(TheModel::getField, 7).print(abstractQuery));
  }

  @Test
  public void testMax() throws Exception {
    assertEquals("max(field)", max(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testMin() throws Exception {
    assertEquals("min(field)", min(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testAvg() throws Exception {
    assertEquals("avg(field)", avg(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testLeftJoin() throws Exception {
    // theta join
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel , "
            + "pl.matsuo.core.model.query.TheModel appointment WHERE (appointment.id = id)",
        query(TheModel.class, leftJoin("appointment", TheModel.class, cond("appointment.id = id")))
            .printQuery());
  }

  @Test
  public void testJoin() throws Exception {
    // theta join
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel JOIN theModel.subModel appointment",
        query(TheModel.class, join("appointment", "theModel.subModel")).printQuery());
  }
}
