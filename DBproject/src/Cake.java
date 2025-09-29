//package database;
//케익 재고 확인하기 기능..?(select 조건을 위해)

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

public class Cake {

	// ID 자동 증가용 변수
	// private static int nextID = 1;

	// 기존 필드 (ERD를 기준으로)
	private String cakeID;
	private String cake_type;
	private int price;
	private String size; // 1호, 2호, 미니 이런식이니까 문자열로.
	private LocalDate made_date; // 날짜 객체, 만든 날 /근데 딱히 사용을 안 하고 있는 거 같음.
	private String ingredientID;
	private int quantity;

	// 남은 재고 관리 필요..
	/*
	 * private static List<Cake> stock = new ArrayList<>();
	 * private int quantity;
	 */ // 데이터베이스와 논리적으로 연결 어려워서 cakeRepository 클래스를 새로 만듦.

	// 주문용 복사 생성자 (quantity 없음)
	public Cake(String cake_type, int price, String size, LocalDate made_date, String ingredientID) {
		// this.cakeID = nextID++;
		this.cake_type = cake_type;
		this.price = price;
		this.size = size;
		this.made_date = made_date;
		this.ingredientID = ingredientID;
		this.quantity = 0; // 또는 필요하면 기본값 설정
	}

	// 생성자(전체포함)
	public Cake(String cakeID, String cake_type, int price, String size, LocalDate made_date, String ingredientID,
			int quantity) {
		// this.cakeID = nextID++;
		this.cakeID = cakeID;
		this.cake_type = cake_type;
		this.price = price;
		this.size = size;
		this.made_date = made_date;
		this.ingredientID = ingredientID;
		this.quantity = quantity;
	}

	// Getter
	public String getCakeID() {
		return cakeID;
	}

	public String getCakeType() {
		return cake_type;
	}

	public int getPrice() {
		return price;
	}

	public String getSize() {
		return size;
	}

	public String getIngredientID() {
		return ingredientID;
	}

	public int getQuantity() {
		return quantity;
	}

	public LocalDate getMade_date() {
		return made_date;
	}

	// setter
	public void setCakeType(String type) {
		this.cake_type = type;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setMadeDate(LocalDate date) {
		this.made_date = date;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	// 매장에 새 케익이 들어왔다! 케익 재고 추가
	/*
	 * 필요없는 메서드. 이제 repository 클래스가 담당.
	 * public static void addToStock(Cake cake) {
	 * stock.add(cake);
	 * }
	 */

	// 재고에서 종류+사이즈 일치하는 케이크 찾기
	/*
	 * 마찬가지 이유로 삭제.
	 * public static Cake findCake(String type, String size) {
	 * for (Cake c : stock) {
	 * if (c.getCakeType().equals(type) && c.getSize().equals(size)) {
	 * return c;
	 * }
	 * }
	 * return null;
	 * }
	 */

	// 테스트용 메서드, 케이크 + 재료 iD 잘 matching 되었나 확인용
	// 케익 정보 출력하는 함수
	public void printCakeInfo() {
		System.out.println("========== 케이크 정보 ==========");
		System.out.println("ID: " + cakeID);
		System.out.println("종류: " + cake_type);
		System.out.println("사이즈: " + size);
		System.out.println("가격: " + price + "원");
		System.out.println("수량: " + quantity + "개");
		System.out.println("재료 ID: " + ingredientID);
		System.out.println("제조일자: " + made_date);
		System.out.println("================================");
	}

	public boolean hasEnoughQuantity(int requested) {
		return this.quantity >= requested;
	}

	public void reduceQuantity(int amount) {
		this.quantity -= amount;
	}

	public void addQuantity(int amount) {
		this.quantity += amount;
	}

}
