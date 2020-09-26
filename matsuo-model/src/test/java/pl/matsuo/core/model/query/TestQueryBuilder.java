package pl.matsuo.core.model.query;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static pl.matsuo.core.model.query.QueryBuilder.and;
import static pl.matsuo.core.model.query.QueryBuilder.avg;
import static pl.matsuo.core.model.query.QueryBuilder.between;
import static pl.matsuo.core.model.query.QueryBuilder.cond;
import static pl.matsuo.core.model.query.QueryBuilder.eq;
import static pl.matsuo.core.model.query.QueryBuilder.eqOrIsNull;
import static pl.matsuo.core.model.query.QueryBuilder.ge;
import static pl.matsuo.core.model.query.QueryBuilder.gt;
import static pl.matsuo.core.model.query.QueryBuilder.ilike;
import static pl.matsuo.core.model.query.QueryBuilder.in;
import static pl.matsuo.core.model.query.QueryBuilder.isNotNull;
import static pl.matsuo.core.model.query.QueryBuilder.isNull;
import static pl.matsuo.core.model.query.QueryBuilder.join;
import static pl.matsuo.core.model.query.QueryBuilder.le;
import static pl.matsuo.core.model.query.QueryBuilder.leftJoin;
import static pl.matsuo.core.model.query.QueryBuilder.lt;
import static pl.matsuo.core.model.query.QueryBuilder.max;
import static pl.matsuo.core.model.query.QueryBuilder.maybe;
import static pl.matsuo.core.model.query.QueryBuilder.maybeEq;
import static pl.matsuo.core.model.query.QueryBuilder.memberOf;
import static pl.matsuo.core.model.query.QueryBuilder.min;
import static pl.matsuo.core.model.query.QueryBuilder.ne;
import static pl.matsuo.core.model.query.QueryBuilder.not;
import static pl.matsuo.core.model.query.QueryBuilder.operator;
import static pl.matsuo.core.model.query.QueryBuilder.or;
import static pl.matsuo.core.model.query.QueryBuilder.query;
import static pl.matsuo.core.model.query.QueryBuilder.select;

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
  public void testQuery() {
    // podstawowe zapytani
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel", query(TheModel.class).printQuery());
  }

  @Test
  public void testMaybe() {
    assertEquals("test = '1'", maybe(true, eq(TheModel::getTest, "1")).print(abstractQuery));
    assertNull(maybe(false, eq(TheModel::getTest, "1")));
  }

  @Test
  public void testMaybeEq() {
    assertEquals("test = '1'", maybeEq("1", TheModel::getTest).print(abstractQuery));
    assertNull(maybeEq(null, TheModel::getTest));
  }

  @Test
  public void testMaybe1() {
    assertEquals("test = '1'", maybe("1", eq(TheModel::getTest, "1")).print(abstractQuery));
    assertNull(maybe(null, eq(TheModel::getTest, null)));
  }

  @Test
  public void testSelect() {
    // klauzula select
    assertEquals(
        "SELECT theModel.id, theModel FROM pl.matsuo.core.model.query.TheModel theModel",
        query(TheModel.class, select("theModel.id, theModel")).printQuery());
  }

  @Test
  public void testOperator() {
    assertEquals("field %% 1", operator(TheModel::getField, "%%", 1).print(abstractQuery));
  }

  @Test
  public void testEq() {
    assertEquals("field = 1", eq(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testNe() {
    assertEquals("field != 1", ne(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testGt() {
    assertEquals("field > 1", gt(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testGe() {
    assertEquals("field >= 1", ge(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testLt() {
    assertEquals("field < 1", lt(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testLe() {
    assertEquals("field <= 1", le(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testIlike() {
    assertEquals("lower(field) like '%1%'", ilike(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testIlike1() {
    assertEquals(
        "lower(field) like '1%'",
        ilike(TheModel::getField, 1, MatchMode.START).print(abstractQuery));
    assertEquals(
        "lower(field) like '%1'", ilike(TheModel::getField, 1, MatchMode.END).print(abstractQuery));
  }

  @Test
  public void testIn() {
    assertEquals(
        "field in (1, 2, 3)", in(TheModel::getField, asList(1, 2, 3)).print(abstractQuery));
  }

  @Test
  public void testIn1() {
    assertEquals(
        "field in (1, 2, 3)", in(TheModel::getField, new Object[] {1, 2, 3}).print(abstractQuery));
  }

  @Test
  public void testIn2() {
    assertEquals(
        "field in (FROM pl.matsuo.core.model.query.TheModel theModel WHERE id = 7)",
        in(TheModel::getField, query(TheModel.class, eq(TheModel::getId, 7))).print(abstractQuery));
  }

  @Test
  public void testBetween() {
    assertEquals("field BETWEEN 1 AND 10", between(TheModel::getField, 1, 10).print(abstractQuery));
  }

  @Test
  public void testIsNull() {
    assertEquals("field IS NULL", isNull(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testIsNotNull() {
    assertEquals("field IS NOT NULL", isNotNull(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testEqOrIsNull() {
    assertEquals(
        "(field = 1 OR field IS NULL)", eqOrIsNull(TheModel::getField, 1).print(abstractQuery));
  }

  @Test
  public void testOr() {
    assertEquals(
        "(f1 = 1 OR f2 = 2)",
        or(eq(TheModel::getF1, 1), eq(TheModel::getF2, 2)).print(abstractQuery));
  }

  @Test
  public void testAnd() {
    // warunek and
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel WHERE (id = 12 AND id < 200)",
        query(TheModel.class, and(eq(TheModel::getId, 12), lt(TheModel::getId, 200))).printQuery());
  }

  @Test
  public void testNot() {
    assertEquals("NOT field = 1", not(eq(TheModel::getField, 1)).print(abstractQuery));
  }

  @Test
  public void testCond() {
    assertEquals("(test_condition)", cond("test_condition").print(abstractQuery));
  }

  @Test
  public void testMemberOf() {
    assertEquals("field MEMBER OF 7", memberOf(TheModel::getField, 7).print(abstractQuery));
  }

  @Test
  public void testMax() {
    assertEquals("max(field)", max(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testMin() {
    assertEquals("min(field)", min(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testAvg() {
    assertEquals("avg(field)", avg(TheModel::getField).print(abstractQuery));
  }

  @Test
  public void testLeftJoin() {
    // theta join
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel , "
            + "pl.matsuo.core.model.query.TheModel appointment WHERE (appointment.id = id)",
        query(TheModel.class, leftJoin("appointment", TheModel.class, cond("appointment.id = id")))
            .printQuery());
  }

  @Test
  public void testJoin() {
    // theta join
    assertEquals(
        "FROM pl.matsuo.core.model.query.TheModel theModel JOIN theModel.subModel appointment",
        query(TheModel.class, join("appointment", "theModel.subModel")).printQuery());
  }
}
