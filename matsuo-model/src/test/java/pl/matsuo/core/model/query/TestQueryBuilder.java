package pl.matsuo.core.model.query;

import org.hibernate.criterion.MatchMode;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static java.util.Arrays.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static pl.matsuo.core.model.query.QueryBuilder.*;


/**
 * Created by tunguski on 26.09.13.
 */
public class TestQueryBuilder {


  AbstractQuery abstractQuery = new AbstractQuery(TheModel.class);


  @Test
  public void testQueries() {
    // klauzula where
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel WHERE id = 12",
        query(TheModel.class, eq("id", 12)).printQuery());
    // klauzula order by
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel ORDER BY id DESC",
        query(TheModel.class).orderBy("id DESC").printQuery());
  }


  @Test
  public void testQuery() throws Exception {
    // podstawowe zapytani
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel", query(TheModel.class).printQuery());
  }


  @Test
  public void testMaybe() throws Exception {
    assertEquals("test = '1'", maybe(true, eq("test", "1")).print(abstractQuery));
    assertNull(maybe(false, eq("test", "1")));
  }


  @Test
  public void testMaybeEq() throws Exception {
    assertEquals("test = '1'", maybeEq("1", "test").print(abstractQuery));
    assertNull(maybeEq(null, "test"));
  }


  @Test
  public void testMaybe1() throws Exception {
    assertEquals("test = '1'", maybe("1", eq("test", "1")).print(abstractQuery));
    assertNull(maybe(null, eq("test", null)));
  }


  @Test
  public void testSelect() throws Exception {
    // klauzula select
    assertEquals("SELECT theModel.id, theModel FROM pl.matsuo.core.model.query.TheModel theModel",
        query(TheModel.class, select("theModel.id, theModel")).printQuery());
  }

  @Test
  public void testOperator() throws Exception {
    assertEquals("field %% 1", operator("field", "%%", 1).print(abstractQuery));
  }

  @Test
  public void testEq() throws Exception {
    assertEquals("field = 1", eq("field", 1).print(abstractQuery));
  }

  @Test
  public void testNe() throws Exception {
    assertEquals("field != 1", ne("field", 1).print(abstractQuery));
  }

  @Test
  public void testGt() throws Exception {
    assertEquals("field > 1", gt("field", 1).print(abstractQuery));
  }

  @Test
  public void testGe() throws Exception {
    assertEquals("field >= 1", ge("field", 1).print(abstractQuery));
  }

  @Test
  public void testLt() throws Exception {
    assertEquals("field < 1", lt("field", 1).print(abstractQuery));
  }

  @Test
  public void testLe() throws Exception {
    assertEquals("field <= 1", le("field", 1).print(abstractQuery));
  }

  @Test
  public void testIlike() throws Exception {
    assertEquals("lower(field) like '%1%'", ilike("field", 1).print(abstractQuery));
  }

  @Test
  public void testIlike1() throws Exception {
    assertEquals("lower(field) like '1%'", ilike("field", 1, MatchMode.START).print(abstractQuery));
    assertEquals("lower(field) like '%1'", ilike("field", 1, MatchMode.END).print(abstractQuery));
  }

  @Test
  public void testIn() throws Exception {
    assertEquals("field in (1, 2, 3)", in("field", asList(1, 2, 3)).print(abstractQuery));

  }

  @Test
  public void testIn1() throws Exception {
    assertEquals("field in (1, 2, 3)", in("field", new Object[] { 1, 2, 3 }).print(abstractQuery));
  }

  @Test
  public void testIn2() throws Exception {
    assertEquals("field in (FROM pl.matsuo.core.model.query.TheModel theModel WHERE id = 7)",
        in("field", query(TheModel.class, eq("id", 7))).print(abstractQuery));
  }

  @Test
  public void testBetween() throws Exception {
    assertEquals("field BETWEEN 1 AND 10", between("field", 1, 10).print(abstractQuery));
  }

  @Test
  public void testIsNull() throws Exception {
    assertEquals("field IS NULL", isNull("field").print(abstractQuery));
  }

  @Test
  public void testIsNotNull() throws Exception {
    assertEquals("field IS NOT NULL", isNotNull("field").print(abstractQuery));
  }

  @Test
  public void testEqOrIsNull() throws Exception {
    assertEquals("(field = 1 OR field IS NULL)", eqOrIsNull("field", 1).print(abstractQuery));
  }

  @Test
  public void testOr() throws Exception {
    assertEquals("(f1 = 1 OR f2 = 2)", or(eq("f1", 1), eq("f2", 2)).print(abstractQuery));
  }


  @Test
  public void testAnd() throws Exception {
    // warunek and
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel WHERE (id = 12 AND id < 200)",
        query(TheModel.class, and(eq("id", 12), lt("id", 200))).printQuery());
  }


  @Test
  public void testNot() throws Exception {
    assertEquals("NOT field = 1", not(eq("field", 1)).print(abstractQuery));
  }


  @Test
  public void testCond() throws Exception {
    assertEquals("(test_condition)", cond("test_condition").print(abstractQuery));
  }


  @Test
  public void testMemberOf() throws Exception {
    assertEquals("field MEMBER OF 7", memberOf("field", 7).print(abstractQuery));
  }


  @Test
  public void testMax() throws Exception {
    assertEquals("max(field)", max("field").print(abstractQuery));
  }


  @Test
  public void testMin() throws Exception {
    assertEquals("min(field)", min("field").print(abstractQuery));
  }


  @Test
  public void testAvg() throws Exception {
    assertEquals("avg(field)", avg("field").print(abstractQuery));
  }


  @Test
  public void testLeftJoin() throws Exception {
    // theta join
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel , " +
            "pl.matsuo.core.model.query.TheModel appointment WHERE (appointment.id = id)",
        query(TheModel.class, leftJoin("appointment", TheModel.class, cond("appointment.id = id"))).printQuery());
  }


  @Test
  public void testJoin() throws Exception {
    // theta join
    assertEquals("FROM pl.matsuo.core.model.query.TheModel theModel JOIN theModel.subModel appointment",
        query(TheModel.class, join("appointment", "theModel.subModel")).printQuery());
  }
}

