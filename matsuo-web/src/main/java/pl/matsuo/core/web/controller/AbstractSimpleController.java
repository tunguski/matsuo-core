package pl.matsuo.core.web.controller;

import pl.matsuo.core.model.AbstractEntity;
import pl.matsuo.core.params.IQueryRequestParams;

/** Created by tunguski on 23.11.13. */
public class AbstractSimpleController<E extends AbstractEntity>
    extends AbstractController<E, IQueryRequestParams> {}
