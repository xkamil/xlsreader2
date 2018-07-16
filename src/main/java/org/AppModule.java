package org;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.controller.view.ProgressViewController;
import org.controller.view.section.ResultSectionController;
import org.model.DataModel;
import org.model.filter.CellFilter;
import org.model.filter.Filter;
import org.util.WorkbookReader;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataModel.class).in(Singleton.class);
        bind(WorkbookReader.class).in(Singleton.class);
        bind(Filter.class).to(CellFilter.class).in(Singleton.class);
        bind(ResultSectionController.class).in(Singleton.class);
        bind(ProgressViewController.class).in(Singleton.class);
    }
}
