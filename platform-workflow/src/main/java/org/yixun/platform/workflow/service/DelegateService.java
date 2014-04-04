package org.yixun.platform.workflow.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class DelegateService {
    private static Logger logger = LoggerFactory
            .getLogger(DelegateService.class);
    public static final String SQL_GET_DELEGATE_INFO = "select * from bpm_delegate_info"
            + " where status=1 and assignee=? order by id desc";
    public static final String SQL_SET_DELEGATE_INFO = "insert into bpm_delegate_history"
            + "(assignee,attorney,delegate_time,task_id,status) values(?,?,now(),?,1)";
    @Inject
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getDelegateInfo(String targetAssignee,
            String targetProcessDefinitionId) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                SQL_GET_DELEGATE_INFO, targetAssignee);

        for (Map<String, Object> map : list) {
            logger.info("map : {}", map);

           
            String attorney = (String) map.get("attorney");
            String processDefinitionId = (String) map
                    .get("process_definition_id");
            Date startTime = (Date) map.get("start_time");
            Date endTime = (Date) map.get("end_time");

            if (timeNotBetweenNow(startTime, endTime)) {
                logger.info("timeNotBetweenNow");

                continue;
            }

            if ((processDefinitionId == null)
                    || processDefinitionId.equals(targetProcessDefinitionId)) {
                logger.info("delegate to {}", attorney);
                
                return map;
            }
        }

        return null;
    }

    public void saveRecord(String assignee, String attorney, String taskId) {
        jdbcTemplate.update(SQL_SET_DELEGATE_INFO, assignee, attorney, taskId);
    }

    private boolean timeNotBetweenNow(Date startTime, Date endTime) {
        Date now = new Date();

        if (startTime != null) {
            return now.before(startTime);
        }

        if (endTime != null) {
            return now.after(endTime);
        }

        return false;
    }

}
