/*
 *      wReport - An Sponge plugin to report bad players and start a vote kick. <https://github.com/JonathanxD/io.github.jonathanxd.wreport.wReport/>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2016 TheRealBuggy/JonathanxD (Jonathan Ribeiro Lopes) <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package io.github.jonathanxd.wreport.config;

import com.google.common.reflect.TypeToken;

import com.github.jonathanxd.iutils.reflection.RClass;
import com.github.jonathanxd.iutils.reflection.Reflection;

import org.spongepowered.api.entity.living.player.User;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import io.github.jonathanxd.wreport.actions.ActionData;
import io.github.jonathanxd.wreport.reports.CloseReportData;
import io.github.jonathanxd.wreport.reports.Report;
import io.github.jonathanxd.wreport.reports.ReportType;
import io.github.jonathanxd.wreport.reports.reasons.Reason;
import io.github.jonathanxd.wreport.utils.func.ThrowingConsumer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

/**
 * Created by jonathan on 03/12/15.
 */
public class ReportSerializer implements TypeSerializer<Report> {
    private static final String ID_NODE = "id";
    private static final String TYPE_NODE = "type";
    private static final String REASON_NODE = "reason";
    private static final String INVOLVED_NODE = "involved";
    private static final String APPLICANT_NODE = "applicant";
    private static final String CREATING_DATE_NODE = "creationDate";
    private static final String CLOSED_REPORT_NODE = "closed";
    private static final String DESCRIPTION_NODE = "description";
    private static final TypeToken<Collection<User>> USERS_TOKEN = new TypeToken<Collection<User>>() {
    };

    @Override
    public Report deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        long id = value.getNode(ID_NODE).getLong();

        String description = value.getNode(DESCRIPTION_NODE).getValue(TypeToken.of(String.class));

        Instant creationDate = value.getNode(CREATING_DATE_NODE).getValue(TypeToken.of(Instant.class));

        Reason reason = value.getNode(REASON_NODE).getValue(TypeToken.of(Reason.class));

        ReportType reportType = value.getNode(TYPE_NODE).getValue(TypeToken.of(ReportType.class));

        CloseReportData closeReportData = value.getNode(CLOSED_REPORT_NODE).getValue(TypeToken.of(CloseReportData.class), (CloseReportData) null);

        User applicant = value.getNode(APPLICANT_NODE).getValue(TypeToken.of(User.class), (User) null);

        Collection<User> reportedUsers = value.getNode(INVOLVED_NODE).getValue(USERS_TOKEN, Collections.emptyList());

        Report report = new Report(id, creationDate, reportType, reason, reportedUsers, applicant, description);

        if (closeReportData != null) {
            try {
                Reflection.changeFinalField(RClass.getRClass(ActionData.class, closeReportData.getAction()), "report", report);
            } catch (Exception e) {
                throw new ObjectMappingException(e);
            }

            report.setCloseReportData(closeReportData);
        }

        return report;
    }

    @Override
    public void serialize(TypeToken<?> type, Report obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode(ID_NODE).setValue(obj.getId());

        value.getNode(DESCRIPTION_NODE).setValue(TypeToken.of(String.class), obj.getDescription());

        value.getNode(CREATING_DATE_NODE).setValue(TypeToken.of(Instant.class), obj.getCreationDate());

        value.getNode(REASON_NODE).setValue(TypeToken.of(Reason.class), obj.getReportReason());

        value.getNode(TYPE_NODE).setValue(TypeToken.of(ReportType.class), obj.getReportType());

        obj.getCloseReportData().ifPresent((ThrowingConsumer<CloseReportData>) c -> value.getNode(CLOSED_REPORT_NODE).setValue(TypeToken.of(CloseReportData.class), c));

        obj.getReportApplicant().ifPresent((ThrowingConsumer<User>) c -> value.getNode(APPLICANT_NODE).setValue(TypeToken.of(User.class), c));

        obj.getReportedPlayers().ifPresent((ThrowingConsumer<Collection<User>>) users -> value.getNode(INVOLVED_NODE).setValue(USERS_TOKEN, users));

    }
}
