package pl.matsuo.core.service.db;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.function.Function;

public interface ISelectDefinition<E> extends Function<JPAQueryFactory, JPAQuery<E>> {}
