package com.wu.intimate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.intimate.mapper.QStarMapper;
import com.wu.intimate.mapper.StarMapper;
import com.wu.intimate.model.QStar;
import com.wu.intimate.model.Star;
import com.wu.intimate.service.QStarService;
import com.wu.intimate.service.StarService;
import org.springframework.stereotype.Service;

@Service
public class QStarServiceImpl extends ServiceImpl<QStarMapper, QStar> implements QStarService {
}
