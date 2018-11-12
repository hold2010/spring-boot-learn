package com.example.demo.test;

import org.apache.ibatis.session.SqlSession;

import com.example.demo.mapper.JobMapper;
import com.example.demo.model.Job;
import com.example.demo.util.SqlSessionFactoryUtil;

public class TestJob {

	public static void main(String[] args) {
		SqlSession sqlSession= SqlSessionFactoryUtil.openSession();
		JobMapper jobMapper = sqlSession.getMapper(JobMapper.class);
		Job my = jobMapper.getJobById(100L);
		sqlSession.commit();
		sqlSession.close();
		System.out.println(my.getJobName());
	}
}
