package com.pnuproject.travellog.Main.MapFragment.Controller.Search;

public class SearchClass {
    //카카오 REST API 로컬 검색을 이용

    String place_user, place_search;

    public SearchClass() {
    }

    /*
      검색 버튼 클릭시 동작
      MapFragment
     */
    public void findPlace(String s1){
        place_user = s1;
    }

    /*
      길찾기 버튼 클릭시 동작
      SearchDialog
     */
    public void findPath(String s1, String s2){
        place_user = s1;
        place_search = s2;
    }
}
