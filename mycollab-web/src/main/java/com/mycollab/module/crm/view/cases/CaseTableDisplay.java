/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.cases;

import com.mycollab.common.TableViewField;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.data.CrmLinkBuilder;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.service.CaseService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.CheckBoxDecor;
import com.mycollab.vaadin.web.ui.LabelLink;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.UserLink;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CaseTableDisplay extends DefaultPagedBeanTable<CaseService, CaseSearchCriteria, SimpleCase> {

    public CaseTableDisplay(List<TableViewField> displayColumns) {
        this(null, displayColumns);
    }

    public CaseTableDisplay(TableViewField requiredColumn, List<TableViewField> displayColumns) {
        this(null, requiredColumn, displayColumns);
    }

    public CaseTableDisplay(String viewId, TableViewField requiredColumn, List<TableViewField> displayColumns) {
        super(AppContextUtil.getSpringBean(CaseService.class), SimpleCase.class, viewId, requiredColumn, displayColumns);

        this.addGeneratedColumn("selected", (source, itemId, columnId) -> {
            final SimpleCase cases = getBeanByIndex(itemId);
            CheckBoxDecor cb = new CheckBoxDecor("", cases.isSelected());
            cb.setImmediate(true);
            cb.addValueChangeListener(valueChangeEvent -> fireSelectItemEvent(cases));
            cases.setExtraData(cb);
            return cb;
        });

        this.addGeneratedColumn("subject", (source, itemId, columnId) -> {
            SimpleCase cases = getBeanByIndex(itemId);
            LabelLink b = new LabelLink(cases.getSubject(), CrmLinkBuilder.generateCasePreviewLinkFull(cases.getId()));

            if (cases.isCompleted()) {
                b.addStyleName(WebUIConstants.LINK_COMPLETED);
            }
            b.setDescription(CrmTooltipGenerator.generateTooltipCases(AppContext.getUserLocale(), cases,
                    AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
            return b;
        });

        this.addGeneratedColumn("accountName", (source, itemId, columnId) -> {
            SimpleCase cases = getBeanByIndex(itemId);
            return new LabelLink(cases.getAccountName(), CrmLinkBuilder.generateAccountPreviewLinkFull(cases.getAccountid()));
        });

        this.addGeneratedColumn("assignUserFullName", (source, itemId, columnId) -> {
            SimpleCase cases = getBeanByIndex(itemId);
            return new UserLink(cases.getAssignuser(), cases.getAssignUserAvatarId(), cases.getAssignUserFullName());
        });

        this.addGeneratedColumn("createdtime", (source, itemId, columnId) -> {
            SimpleCase cases = getBeanByIndex(itemId);
            return new ELabel(AppContext.formatPrettyTime(cases.getCreatedtime())).withDescription(AppContext
                    .formatDateTime(cases.getCreatedtime()));
        });

        this.setWidth("100%");
    }
}
