package kr.co.mapspring.global.exception.favorite;

public class FavoriteScenarioAlreadyExistsException extends RuntimeException {

    public FavoriteScenarioAlreadyExistsException() {
        super("이미 즐겨찾기한 시나리오입니다.");
    }

}
