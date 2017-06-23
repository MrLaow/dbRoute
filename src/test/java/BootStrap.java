import org.janson.dao.IDataSourcePoolDao;
import org.janson.vo.DataSourcePool;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Windows User on 2017/6/22.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class BootStrap extends AbstractJUnit4SpringContextTests {


    private IDataSourcePoolDao dataSourcePoolDao;

    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() {
        dataSourcePoolDao = (IDataSourcePoolDao) applicationContext.getBean("poolDao");
    }


    @Test
    public void run() {

    }

    @Test
    public void select() {
        System.out.println("*****************\n");
        System.out.println(dataSourcePoolDao.selectByPrimaryKey(101));
        DataSourcePool dataSourcePool = new DataSourcePool();
        dataSourcePool.setId(100);
//        dataSourcePool.setUrl("dddddddd");
//         dataSourcePool.setTenant("alibaba");
//        dataSourcePool.setUsername("mayun");
//        dataSourcePool.setPassword("wangtao");
//        dataSourcePool.setDbtype("oracle");
        dataSourcePool.setIseffective(0);
        System.out.println(dataSourcePoolDao.selectByCondition(dataSourcePool));
        System.out.println("\n*****************");
    }

    @Test
    public void insert() {
        System.out.println("*****************\n");
        DataSourcePool dataSourcePool = new DataSourcePool();
        for (int i=100 ;i<110 ;i++) {
            dataSourcePool.setId(i);
            dataSourcePool.setTenant("alibaba"+i);
            dataSourcePool.setUsername("mayun"+i);
            dataSourcePool.setPassword("wangtao"+i);
            dataSourcePool.setUrl("ddddddddddddddddddddd"+i);
            dataSourcePool.setDbtype("oracle");
            dataSourcePool.setIseffective(i%2);
            dataSourcePoolDao.insert(dataSourcePool);
        }

    }

    @Test
    public void delete() {
        System.out.println("*****************\n");
        System.out.println(dataSourcePoolDao.deleteByPrimaryKey(100));
        System.out.println("\n*****************");
    }

    @Test
    public void update() {
        System.out.println("*****************\n");
        DataSourcePool dataSourcePool = new DataSourcePool();
        dataSourcePool.setId(101);
        dataSourcePool.setTenant("alinana");
        dataSourcePool.setUsername("wangtao");
        dataSourcePool.setPassword("mayun");
        dataSourcePool.setUrl("yyyyyyyyy");
        dataSourcePool.setDbtype("mysql");
        dataSourcePool.setIseffective(1);
        dataSourcePoolDao.updateByPrimaryKey(dataSourcePool);
        System.out.println("\n*****************");
    }

    @Test
    public void page() {
        System.out.println("*****************\n");
        DataSourcePool dataSourcePool = new DataSourcePool();
        dataSourcePool.setTenant("ali");
        System.out.println(dataSourcePoolDao.queryByPage(dataSourcePool,5,2));
        System.out.println("\n*****************");
    }
}
