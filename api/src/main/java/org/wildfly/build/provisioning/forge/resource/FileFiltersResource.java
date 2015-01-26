package org.wildfly.build.provisioning.forge.resource;

import org.wildfly.build.common.model.FileFilter;

import java.util.List;

/**
 * A resource which has file filters.
 * @author Eduardo Martins
 */
public interface FileFiltersResource {

    /**
     *
     * @param filter
     * @return
     */
    boolean addFilter(FileFilter filter);

    /**
     *
     * @return
     */
    List<FileFilter> getFilters();

    /**
     *
     * @param filter
     * @return
     */
    boolean removeFilter(FileFilter filter);
}
