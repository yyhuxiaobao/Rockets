package com.pauper.straw.es;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

@SpringBootTest
public class EsTest {

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void test(){
        /*IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Item.class);
        if(!indexOperations.exists()) { // 当前索引不存在
            boolean result1 =indexOperations.create();// 只是创建索引 。mappings没有映射
            boolean result2 = indexOperations.putMapping();// 映射属性
            System.out.println("创建结果：" + ",映射结果：" + result2);
        }else {
            System.out.println("文档已存在");
        }*/
        /*for(int i = 1 ; i<=50; i++){
            Item item = new Item();
            item.setId(Integer.valueOf(i).longValue());
            item.setBrand("品牌");
            item.setTitle("标题" + i);
            item.setImages("12312321");
            item.setPrice(Integer.valueOf(i).doubleValue());
            Item item1 = elasticsearchRestTemplate.save(item);
            System.out.println(item1.getTitle());
        }*/

        // 创建查询  字段--索引
        NativeSearchQuery searchQuery=new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("title", "1")).build();    //
        // 查询
        SearchHits<Item> search1 = elasticsearchRestTemplate.search(searchQuery, Item.class);
        search1.forEach(System.out::println);


    }
}
