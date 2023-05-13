package com.wu.intimate.config;
 
import com.wu.intimate.mapper.ConsultantMapper;
import com.wu.intimate.model.Consultant;
import com.wu.intimate.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时任务
 * @author pan_junbiao
 **/
@Component
public class Task {
    @Autowired
    private ConsultantMapper consultantMapper;
    @Autowired
    private ConsultantService consultantServiceImpl;

    @Scheduled(cron="0 0 0 * * ?")   //凌晨执行一次
    public void updateConDate(){
        List<Integer> allId = consultantMapper.findAllId();
        for(Integer id : allId){
            Consultant consultant = new Consultant();
            consultant.setId(id);
            consultant.setConDate("00000000000000000000");
            consultantMapper.updateById(consultant);
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
        System.out.println("更改时间成功 " + df.format(new Date()));
    }
}