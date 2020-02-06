/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2019 Serge Rider (serge@jkiss.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkiss.dbeaver.model.sql.generator;

import org.jkiss.dbeaver.DBException;
import org.jkiss.dbeaver.model.DBPEvaluationContext;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.runtime.DBRProgressMonitor;
import org.jkiss.dbeaver.model.struct.DBSEntity;
import org.jkiss.dbeaver.model.struct.DBSEntityAttribute;

public class SQLGeneratorInsert extends TableAnalysisRunner {

    @Override
    public void generateSQL(DBRProgressMonitor monitor, StringBuilder sql, DBSEntity object) throws DBException {

        sql.append("INSERT INTO ").append(getEntityName(object)).append(getLineSeparator()).append("(");
        boolean hasAttr = false;
        for (DBSEntityAttribute attr : getAllAttributes(monitor, object)) {
            if (DBUtils.isPseudoAttribute(attr) || DBUtils.isHiddenObject(attr) || attr.isAutoGenerated()) {
                continue;
            }
            if (hasAttr) sql.append(", ");
            sql.append(DBUtils.getObjectFullName(attr, DBPEvaluationContext.DML));
            hasAttr = true;
        }
        sql.append(")").append(getLineSeparator()).append("VALUES(");
        hasAttr = false;
        for (DBSEntityAttribute attr : getAllAttributes(monitor, object)) {
            if (DBUtils.isPseudoAttribute(attr) || DBUtils.isHiddenObject(attr) || attr.isAutoGenerated()) {
                continue;
            }
            if (hasAttr) sql.append(", ");
            appendDefaultValue(sql, attr);
            hasAttr = true;
        }
        sql.append(");\n");
    }

}
