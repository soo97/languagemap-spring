package kr.co.mapspring.global.exception.favorite;

public class FavoriteScenarioNotFoundException extends RuntimeException {

    public FavoriteScenarioNotFoundException() {
        super("존재하지 않는 즐겨찾기 시나리오입니다.");
    }

}
