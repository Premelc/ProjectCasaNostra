package com.example.cn;

import com.example.cn.model.Swipe;
import com.example.cn.sorting.SimilarityGradeSorting;
import com.example.cn.sorting.UsableActiveUser;
import com.example.cn.sorting.UsableOtherUser;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class SimilarityGradeSortingTest {
    //Execute(UsableActiveUser actUsr, List<UsableOtherUser> othrUsr)
    static SimilarityGradeSorting sgs = new SimilarityGradeSorting();

    @Test
    public void testHaveApt(){
        List<UsableOtherUser> othrUsrs = new ArrayList<>();
        UsableActiveUser actUsr = new UsableActiveUser(1,"Premo","premo@gmail.com","premo","Premo",1999,"",'M',1,false,false,false,'S',1999,2000,true,true,true,1,2000,true, new int[]{0, 0, 0, 0});
        //user with apt
        UsableOtherUser user1 = new UsableOtherUser(2,"nikola",1999,"",'M',1,true,false,true,true,new int[]{0,0,0,0},5000,true,2);
        //user without apt
        UsableOtherUser user2 = new UsableOtherUser(3,"jelena",1999,"",'Ž',1,true,false,true,true,new int[]{0,0,0,0},5000,false,2);
        List<Swipe> swipeState = new ArrayList<>();
        othrUsrs.add(user1);
        othrUsrs.add(user2);

        othrUsrs= sgs.GradingTestInterface(actUsr,othrUsrs ,swipeState);

        assertEquals(1, othrUsrs.size());
    }
    @Test
    public void testNeedApt(){
        List<UsableOtherUser> othrUsrs = new ArrayList<>();
        UsableActiveUser actUsr = new UsableActiveUser(1,"Premo","premo@gmail.com","premo","Premo",1999,"",'M',1,false,false,false,'S',1999,2000,true,true,false,1,2000,true, new int[]{0, 0, 0, 0});
        //user with apt
        UsableOtherUser user1 = new UsableOtherUser(2,"nikola",1999,"",'M',1,true,false,true,true,new int[]{0,0,0,0},5000,true,2);
        //user without apt
        UsableOtherUser user2 = new UsableOtherUser(3,"jelena",1999,"",'Ž',1,true,false,true,true,new int[]{0,0,0,0},5000,false,2);
        List<Swipe> swipeState = new ArrayList<>();
        othrUsrs.add(user1);
        othrUsrs.add(user2);

        othrUsrs= sgs.GradingTestInterface(actUsr,othrUsrs ,swipeState);

        assertEquals(2, othrUsrs.size());
    }

    @Test
    public void testOnlyOneGender(){
        List<UsableOtherUser> othrUsrs = new ArrayList<>();
        UsableActiveUser actUsr = new UsableActiveUser(1,"Premo","premo@gmail.com","premo","Premo",1999,"",'M',1,false,false,false,'M',1999,2000,true,true,false,1,2000,true, new int[]{0, 0, 0, 0});
        //user with matching gender
        UsableOtherUser user1 = new UsableOtherUser(2,"nikola",1999,"",'M',1,true,false,true,true,new int[]{0,0,0,0},5000,true,2);
        //user with gender missmatch
        UsableOtherUser user2 = new UsableOtherUser(3,"jelena",1999,"",'Ž',1,true,false,true,true,new int[]{0,0,0,0},5000,false,2);
        List<Swipe> swipeState = new ArrayList<>();
        othrUsrs.add(user1);
        othrUsrs.add(user2);

        othrUsrs= sgs.GradingTestInterface(actUsr,othrUsrs ,swipeState);

        assertEquals(1, othrUsrs.size());
    }

    @Test
    public void testNoElegibleUsers(){
        List<UsableOtherUser> othrUsrs = new ArrayList<>();
        UsableActiveUser actUsr = new UsableActiveUser(1,"Premo","premo@gmail.com","premo","Premo",1999,"",'M',1,false,false,false,'S',1999,2000,true,true,true,1,2000,true, new int[]{0, 0, 0, 0});
        //non elegible user according to explicit requests
        UsableOtherUser user1 = new UsableOtherUser(2,"nikola",1999,"",'M',1,true,false,true,true,new int[]{0,0,0,0},5000,true,2);
        //non elegible user according to explicit requests
        UsableOtherUser user2 = new UsableOtherUser(3,"jelena",1999,"",'Ž',1,true,false,true,true,new int[]{0,0,0,0},5000,true,2);
        List<Swipe> swipeState = new ArrayList<>();
        othrUsrs.add(user1);
        othrUsrs.add(user2);

        othrUsrs= sgs.GradingTestInterface(actUsr,othrUsrs ,swipeState);

        assertEquals(0, othrUsrs.size());
    }

    @Test
    public void testGrading(){
        List<UsableOtherUser> othrUsrs = new ArrayList<>();
        UsableActiveUser actUsr = new UsableActiveUser(1,"Premo","premo@gmail.com","premo","Premo",1999,"",'S',1,false,false,false,'S',1998,2000,true,true,true,1,2000,true, new int[]{0, 0, 0, 0});
        //user with common traits
        UsableOtherUser user1 = new UsableOtherUser(2,"nikola",1999,"",'M',1,false,false,true,true,new int[]{0,0,0,0},5000,false,2);
        //user with less common traits
        UsableOtherUser user2 = new UsableOtherUser(3,"jelena",2001,"",'Ž',2,true,true,true,true,new int[]{0,0,0,0},5000,false,2);
        List<Swipe> swipeState = new ArrayList<>();
        othrUsrs.add(user1);
        othrUsrs.add(user2);

        othrUsrs= sgs.GradingTestInterface(actUsr,othrUsrs ,swipeState);

        assertEquals(2, othrUsrs.get(0).getId_korisnik());
    }

    @Test
    public void testActiveUserSwiped(){
        List<UsableOtherUser> othrUsrs = new ArrayList<>();
        UsableActiveUser actUsr = new UsableActiveUser(1,"Premo","premo@gmail.com","premo","Premo",1999,"",'S',1,false,false,false,'S',1998,2000,true,true,false,1,2000,true, new int[]{0, 0, 0, 0});
        //user with common traits
        UsableOtherUser user1 = new UsableOtherUser(2,"nikola",1999,"",'M',1,false,false,true,true,new int[]{0,0,0,0},5000,false,2);
        //user with less common traits
        UsableOtherUser user2 = new UsableOtherUser(3,"jelena",2001,"",'Ž',2,true,true,true,true,new int[]{0,0,0,0},5000,false,2);

        //User that the active user had swiped already
        List<Swipe> swipeState = new ArrayList<>();
        Swipe swp = new Swipe();
        swp.setId_1(1);
        swp.setId_2(2);
        swp.setSwipe_1(true);
        swipeState.add(swp);

        //User that hasn't been swiped by the active user yet
        Swipe swp2 = new Swipe();
        swp2.setId_1(1);
        swp2.setId_2(3);
        swipeState.add(swp2);

        othrUsrs.add(user1);
        othrUsrs.add(user2);

        othrUsrs= sgs.GradingTestInterface(actUsr,othrUsrs ,swipeState);

        assertEquals(1, othrUsrs.size());
    }

    @Test
    public void testActiveUserWasSwiped() {
        List<UsableOtherUser> othrUsrs = new ArrayList<>();
        UsableActiveUser actUsr = new UsableActiveUser(1, "Premo", "premo@gmail.com", "premo", "Premo", 1999, "", 'S', 1, false, false, false, 'S', 1998, 2000, true, true, false, 1, 2000, true, new int[]{0, 0, 0, 0});
        //user with common traits
        UsableOtherUser user1 = new UsableOtherUser(2, "nikola", 1999, "", 'M', 1, false, false, true, true, new int[]{0, 0, 0, 0}, 5000, false, 2);
        //user with less common traits
        UsableOtherUser user2 = new UsableOtherUser(3, "jelena", 2001, "", 'Ž', 2, true, true, true, true, new int[]{0, 0, 0, 0}, 5000, false, 2);

        //User that the active user had swiped already
        List<Swipe> swipeState = new ArrayList<>();
        Swipe swp = new Swipe();
        swp.setId_1(1);
        swp.setId_2(2);
        swp.setSwipe_2(true);
        swipeState.add(swp);

        //User that hasn't been swiped by the active user yet
        Swipe swp2 = new Swipe();
        swp2.setId_1(1);
        swp2.setId_2(3);
        swp.setSwipe_2(false);
        swipeState.add(swp2);

        othrUsrs.add(user1);
        othrUsrs.add(user2);

        othrUsrs = sgs.GradingTestInterface(actUsr, othrUsrs, swipeState);

        assertEquals(2, othrUsrs.size());
    }

}
