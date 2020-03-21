package pl.matsuo.core.web.controller;

import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.params.IQueryRequestParams;

public class AbstractSimpleController<E extends AbstractEntity>
    extends AbstractController<E, IQueryRequestParams> {}
