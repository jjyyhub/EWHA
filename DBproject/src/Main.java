//package database;

import java.util.Scanner;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Main {

	static Scanner s = new Scanner(System.in);
	static List<Order> orderList = new ArrayList<>();
	static List<Customer> customerList = new ArrayList<>();

	// menu1 고객측 / 매장측
	private static int selectMenu1() {
		int menu1;
		System.out.println("[케이크 예약]");
		System.out.println("--메뉴(고객/매장 선택)--");
		System.out.println("1.고객측 메뉴 \n2.매장측 메뉴 \n0.종료하기");
		System.out.println();

		System.out.print("메뉴 번호를 선택해주세요. >>");
		menu1 = s.nextInt();
		System.out.println();

		return menu1;
	}

	// menu1_0, 고객측 - 신규예약? 예약 수정?
	private static int selectMenu1_0() {
		int menu1_0;
		System.out.println("고객측 메뉴를 선택하셨습니다.");
		System.out.println("------메뉴(고객측)------");
		System.out.println("1.신규 예약 \n2.예약 수정");
		System.out.println();

		System.out.print("메뉴 번호를 선택해주세요. >>");
		menu1_0 = s.nextInt();
		System.out.println();

		return menu1_0;
	}

	// menu1_1, 고객측 - 1. 신규 예약
	private static Order selectMenu1_1() {
		System.out.println("[신규예약]");

		// (1) 고객의 id 입력받기
		System.out.print("고객님의 ID를 입력해주세요: ");
		String customerID = s.next(); // ← 기존 하드코딩 제거하고 입력받음
		Customer found = CustomerRepository.findById(customerID);
		// 고객의 id가 만약 테이블에 존재하지 않는다면
		// Customer found = null;
		if (customerID == null) {
			System.out.println("존재하지 않는 고객 ID입니다. 예약을 진행할 수 없습니다.");
			return null;
		}
		// 너 존재 안 하던데? 로 null return 하기.
		s.nextLine();
		// (2) 케익 종류 고르기
		System.out.print("원하는 케익 종류를 선택해주세요(생크림 케이크,딸기 케이크,초코 케이크,말차 케이크,치즈 케이크):");
		String cakeType = s.nextLine();

		// (3) 케익 사이즈 고르기
		System.out.print("케익 사이즈를 선택해주세요.(1호, 2호, 미니 중 택 1):");
		String size = s.nextLine();

		// 근데 남은 케익이 없다면?
		Cake cakeInStock = CakeRepository.findCake(cakeType, size);
		if (cakeInStock == null) {
			System.out.println("해당 케이크는 재고에 없습니다.");
			System.out.println();
			return null;
		}

		// (4) 케익 갯수 고르기
		System.out.print("구매하실 케익 수를 입력하시오.(n) (남은 수량: " + cakeInStock.getQuantity() + "개):");
		int quantity = s.nextInt();

		// 남은 재고보다 많은 케익을 선택할 수 없음...
		if (!CakeRepository.hasEnoughQuantity(cakeInStock, quantity)) {
			System.out.println("죄송합니다. 재고 수량보다 많은 수량을 주문하실 수 없습니다.");
			System.out.println();
			return null;
		}

		// (5)픽업날짜 선택하기
		System.out.print("픽업 날짜를 입력해주세요. (yyyy-mm-dd):");
		String dateStr = s.next();
		LocalDate pickupDate = LocalDate.parse(dateStr);

		// 재고 감소
		CakeRepository.reduceQuantity(cakeInStock, quantity);

		// 케이크는 재고에서 복사해서 사용하기 (getter 이용, 주문별 개체)
		Cake orderedCake = new Cake(
				cakeInStock.getCakeType(),
				cakeInStock.getPrice(),
				cakeInStock.getSize(),
				pickupDate,
				cakeInStock.getIngredientID());

		// 주문 생성
		Order order = Order.createOrder(orderedCake, quantity, found, pickupDate);

		// 여기 추가//
		OrderRepository.addOrder(order); // 신규 예약 저장

		order.printOrderInfo();

		// (6) 결제 수단 선택
		System.out.print("결제 수단을 선택해주세요 (현금/카드/계좌이체): ");
		String method = s.next();

		Payment payment = new Payment(method, order.getOrderID());

		// 여기 추가//
		OrderRepository.addPayment(payment); // 신규 결제 저장

		order.setPayment(payment);

		System.out.println("예약이 완료되었습니다. 예약 ID는 [" + order.getOrderID() + "]입니다.");
		System.out.println("※ 예약 수정/조회 시 필요하니 꼭 기억해주세요.");
		System.out.println();

		return order;
	}

	// menu1_2, 고객측 - 2. 예약 수정
	private static void selectMenu1_2() {
		System.out.println("[예약수정]");
		System.out.print("예약 ID를 입력해주세요: ");
		String inputId = s.next();

		// 내 orderID를 찾을 때까지 for문 돌리기
		Order found = null;
		for (Order o : orderList) {
			if (o.getOrderID().equals(inputId)) {
				found = o;
				break;
			}
		}

		if (found == null) {
			System.out.println("해당 예약을 찾을 수 없습니다.");
			return;
		}

		// 고객님의 예약내역 보여드리기.
		System.out.println("고객님의 예약내역입니다. 수정할 항목을 골라주세요.");
		found.printOrderInfo();

		System.out.println("--수정할 항목--");
		System.out.println("0. 케이크 종류 + 사이즈");
		System.out.println("1. 수량");
		System.out.println("2. 픽업 날짜");
		System.out.println();
		System.out.print("메뉴 번호를 선택해주세요. >>");
		int sel = s.nextInt();

		// 기존 정보
		String prevType = found.getCake().getCakeType();
		String prevSize = found.getCake().getSize();
		int prevQty = found.getQuantity();
		Cake prevCakeInStock = CakeRepository.findCake(prevType, prevSize);

		switch (sel) {
			case 0:
				System.out.print("새로운 케이크 종류: ");
				String newType = s.next();
				System.out.print("새로운 사이즈: ");
				String newSize = s.next();

				Cake newCake = CakeRepository.findCake(newType, newSize);
				if (newCake == null) {
					System.out.println("해당 케이크는 재고에 없습니다.");
					return;
				}
				if (!newCake.hasEnoughQuantity(prevQty)) {
					System.out.println("재고 부족으로 변경할 수 없습니다.");
					return;
				}

				// 기존 수량 재고 복구 → 새 재고 차감
				if (prevCakeInStock != null)
					prevCakeInStock.addQuantity(prevQty);
				newCake.reduceQuantity(prevQty);

				found.getCake().setCakeType(newType);
				found.getCake().setSize(newSize);
				found.getCake().setMadeDate(found.getPickupDate());
				found.getCake().setPrice(newCake.getPrice()); // 가격도 바꾸기
				found.updateTotalPrice(); // ✅ 가격 반영

				// 여기 추가 //
				OrderRepository.updateCakeAndSize(found.getOrderID(), found.getCake().getCakeType(),
						found.getCake().getSize(), found.getTotalPrice());
				break;

			case 1:
				System.out.println("현재 예약하신 케이크: ["
						+ found.getCake().getCakeType() + " / "
						+ found.getCake().getSize() + "]");
				System.out.print("새로운 수량을 입력해주세요: ");
				int newQty = s.nextInt();

				if (prevCakeInStock == null) {
					System.out.println("기존 케이크를 찾을 수 없습니다.");
					return;
				}

				// 수량 복구 + 새 수량 재고 확인
				prevCakeInStock.addQuantity(prevQty);
				if (!prevCakeInStock.hasEnoughQuantity(newQty)) {
					System.out.println("해당 케이크의 재고가 부족합니다.");
					System.out.println();
					prevCakeInStock.reduceQuantity(prevQty); // 원복
					return;
				}
				prevCakeInStock.reduceQuantity(newQty);
				found.setQuantity(newQty);
				found.updateTotalPrice();

				// 여기 추가//
				OrderRepository.updateQuantity(found.getOrderID(), found.getQuantity(), found.getTotalPrice());
				break;

			case 2:
				System.out.print("새로운 픽업 날짜 (yyyy-mm-dd): ");
				String newDateStr = s.next();
				LocalDate newDate = LocalDate.parse(newDateStr);
				found.setPickupDate(newDate);
				found.getCake().setMadeDate(newDate);

				// 여기 추가//
				OrderRepository.updatePickupDate(found.getOrderID(), found.getPickupDate());
				break;

			default:
				System.out.println("잘못된 선택입니다.");
				return;
		}

		System.out.println("✅ 수정이 완료되었습니다. 다음은 수정된 예약 내역입니다:");
		found.printOrderInfo();

	}
	// menu 1_2 끝..

	// menu2_0 매장측 메뉴 시작.
	private static int selectMenu2_0() {
		System.out.println("매장측 메뉴를 선택하셨습니다.");
		System.out.println("------메뉴(매장측)------");
		System.out.println("1. 고객 주문 내역 조회"); // select 1번 메뉴 수정
		System.out.println("2. 재료 재고 확인");
		System.out.println("3. 재료 재고 추가");
		System.out.println("4. 유통기한 지난 재료 삭제");
		System.out.println("5. 예약 취소하기");
		System.out.println("6. 월별 정산하기"); // select 2번
		System.out.println("7. 케이크 재고 확인"); // select 3번
		System.out.println("8. 결제 방식 별 고객 확인"); // select 5번

		System.out.println();

		System.out.print("메뉴 번호를 선택해주세요. >>");
		return s.nextInt();
	}

	// 여기 변경//
	// 사실상 menu2_1 특정 고객 reservation 내역 확인
	private static void selectMenu2_1() {

		System.out.print("조회할 예약 ID를 입력해주세요: ");
		String resID = s.next();

		OrderInfo order = OrderRepository.getOrderInformation(resID);
		if (order == null) {
			System.out.println("해당 예약을 찾을 수 없습니다.");
		} else {
			System.out.println(order);
		}
	}
	// System.out.println("[픽업 예정 고객 목록]");
	// LocalDate today = LocalDate.now();
	// List<String> printedCustomerIds = new ArrayList<>();

	// for (Order o : orderList) {
	// if (o.getPickupDate().isAfter(today) || o.getPickupDate().isEqual(today)) {
	// String id = o.getCustomerID();
	// if (!printedCustomerIds.contains(id)) {
	// for (Customer c : customerList) {
	// if (c.getCustomerID().equals(id)) {
	// System.out.println("고객 ID: " + c.getCustomerID() + ", 이름: " +
	// c.getCustomerName());
	// printedCustomerIds.add(id);
	// break;
	// }
	// }
	// }
	// }
	// }
	// System.out.println();

	// menu2_2 : 재료 재고 확인
	private static void selectMenu2_2() {
		System.out.println("[현재 재료 재고 목록]");
		List<Ingredient> stock = IngredientRepository.getAllIngredients();

		if (stock.isEmpty()) {
			System.out.println("현재 등록된 재료가 없습니다.");
		} else {
			for (Ingredient i : stock) {
				System.out.println("ID: " + i.getIgdID() +
						", 이름: " + i.getIgdName() +
						", 수량: " + i.getIgdQuantity() +
						", 유통기한: " + i.getExpDate());
			}
		}
		System.out.println();
	}

	// selectMenu2_3 : 재료 추가
	private static void selectMenu2_3() {
		System.out.print("재료 ID: ");
		String id = s.next();
		System.out.print("재료 이름: ");
		String name = s.next();
		System.out.print("수량: ");
		int qty = s.nextInt();
		System.out.print("유통기한 (yyyy-mm-dd): ");
		LocalDate exp = LocalDate.parse(s.next());

		IngredientRepository.addIngredient(new Ingredient(id, name, qty, exp));
		System.out.println("재료가 추가되었습니다.\n");
	}

	// menu2_4는 함수 필요x

	// 여기 변경//
	// menu2_5 : 예약 취소 함수
	private static void selectMenu2_5() {
		System.out.println("[예약 취소]");

		System.out.print("취소할 예약 ID를 입력해주세요: ");
		String id = s.next();

		try {
			OrderRepository.cancelOrder(id);
			System.out.println("예약이 취소되었습니다.");
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 여기 변경//
	// menu2_6
	private static void selectMenu2_6() {
		System.out.print("정산을 원하는 연도를 입력해주세요. (예: 2025): ");
		int year;
		try {
			year = Integer.parseInt(s.next());
		} catch (NumberFormatException e) {
			System.out.println("연도는 숫자로 입력해주세요.");
			return;
		}

		OrderRepository.MonthlyPayment(year);
	}

	// 여기 변경//
	// menu2_7
	private static void selectMenu2_7() {
		s.nextLine();
		System.out.print("어떤 케이크 재고를 확인하시겠습니까? (예: 딸기 케이크): ");
		String cakeType = s.nextLine();

		List<Cake> cakes = CakeRepository.findCakesByType(cakeType);

		if (cakes.isEmpty()) {
			System.out.println("해당 케이크 종류는 재고에 없습니다.\n");
		} else {
			System.out.println(cakeType + "의 재고 목록");
			System.out.println("----------------------------------");
			for (Cake cake : cakes) {
				System.out.printf("cake_type: %s | size: %s | price: %d | quantity: %d%n",
						cake.getCakeType(),
						cake.getSize(),
						cake.getPrice(),
						cake.getQuantity());
			}
			System.out.println();
		}
	}

	// int size1 = 0, size2 = 0, mini = 0;

	// for (Cake c : CakeRepository.getStock()) {
	// if (c.getCakeType().equals(cakeType)) {
	// switch (c.getSize()) {
	// case "1호":
	// size1 += c.getQuantity();
	// break;
	// case "2호":
	// size2 += c.getQuantity();
	// break;
	// case "미니":
	// mini += c.getQuantity();
	// break;
	// }
	// }
	// }

	// if (size1 == 0 && size2 == 0 && mini == 0) {
	// System.out.println("해당 케이크 종류는 재고에 없습니다.\n");
	// } else {
	// System.out.println("\n[" + cakeType + "] 재고 현황:");
	// System.out.println("1호 : " + size1 + "개");
	// System.out.println("2호 : " + size2 + "개");
	// System.out.println("미니 : " + mini + "개\n");
	// }

	// 여기 변경//
	private static void selectMenu2_8() {
		System.out.print("확인할 결제 방식을 입력해주세요 (현금/카드/계좌이체): ");
		String method = s.next();

		List<PaymentInfo> found = CustomerRepository.findCustomerByPay(method);
		System.out.println("\n[" + method + "]으로 결제한 고객 목록:");

		if (found == null) {
			System.out.println("해당 결제 방식으로 결제한 고객이 없습니다.\n");
		} else {
			for (PaymentInfo info : found) {
				System.out.printf("customerID: %s | total_price: %d | method: %s", info.getCustomerID(),
						info.getTotalPrice(),
						info.getPaymentMethod());
				System.out.println();
			}

		}

	}

	// 확인용 테스트 함수 고객에 last_order_date 잘 붙어있나?
	private static void printAllCustomers() {
		System.out.println("[전체 고객 목록]");
		for (Customer c : customerList) {
			System.out.println("ID: " + c.getCustomerID() +
					", 이름: " + c.getCustomerName() +
					", 전화번호: " + c.getCustomerPhone() +
					", 마지막 주문일: " + (c.getLastOrderDate() != null ? c.getLastOrderDate() : "없음"));
		}
		System.out.println();
	}

	// 확인용 함수 테스트 함수 , 결제수단 ex. 현금인지 계좌이체인지 잘 들어간 거 확인.
	private static void printAllOrdersWithPayment() {
		System.out.println("[전체 주문 내역 + 결제 수단]");
		if (orderList.isEmpty()) {
			System.out.println("주문 내역이 없습니다.");
			return;
		}

		for (Order o : orderList) {
			o.printOrderInfo(); // 기존 주문 정보 출력

			// 결제 수단도 함께 출력
			if (o.getPayment() != null) {
				System.out.println("💳 결제 수단: " + o.getPayment().getPaymentMethod());
				System.out.println("결제 ID: " + o.getPayment().getPaymentID());
			} else {
				System.out.println("❗ 결제 정보가 없습니다.");
			}
			System.out.println("-------------------------------\n");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 테스트용 케이크 / 고객 / 재료 재고

		/// 테스트용 케이크 등록 (이후 삭제 예정)
		// CakeRepository.addCake(new Cake("c001", "초코", 20000, "1호", LocalDate.now(),
		/// 101, 5));
		// CakeRepository.addCake(new Cake("c002", "딸기", 22000, "2호", LocalDate.now(),
		/// 102, 3));
		// CakeRepository.addCake(new Cake("c003", "생크림", 18000, "1호", LocalDate.now(),
		/// 103, 2));

		// main() 내부에 테스트용 고객 등록
		// customerList.add(new Customer("cus1", "홍길동", 234));
		// customerList.add(new Customer("cus2", "김철수", 123));

		// 재료 재고
		IngredientRepository.addIngredient(new Ingredient("i201", "밀가루", 10, LocalDate.now().plusDays(3)));
		IngredientRepository.addIngredient(new Ingredient("i202", "생크림", 5, LocalDate.now().minusDays(1))); // 유통기한 지난 것

		// 확인용 테스트, 케이크에 재료 id 잘 붙어있나?
		/*
		 * 잘...붙어있네요^^
		 * for (Cake c : CakeRepository.getStock()) {
		 * c.printCakeInfo();
		 * }
		 */

		// 확인용 테스트 고객에 last_order_date 잘 붙어있나?
		// printAllCustomers(); //while문 끝나는 곳에 넣으면 반영되는지 확인 가능. 잘 됨.

		while (true) {
			int menu1 = selectMenu1();

			if (menu1 == 0) {
				System.out.println("시스템을 종료합니다.");
				break;
			}

			switch (menu1) {
				case 1: // 고객측 메뉴
					int menu1_0 = selectMenu1_0();

					switch (menu1_0) {
						case 1: // 1. 신규 예약
							Order o = selectMenu1_1();
							if (o != null)
								orderList.add(o); // 주문 저장
							break;

						case 2: // 2. 예약 수정
							selectMenu1_2();
							break;
					}
					break;

				case 2: // 매장측 메뉴
					int menu2_0 = selectMenu2_0();
					switch (menu2_0) {
						case 1: // 예약 한 고객 확인
							selectMenu2_1();
							break;
						case 2: // 재료 재고 확인
							selectMenu2_2();
							break;
						case 3: // 재료 재고 추가
							selectMenu2_3();
							break;
						case 4: // 유통기한 지난 재료 삭제
							IngredientRepository.deleteExpiredIngredients();
							System.out.println("유통기한이 지난 재료가 삭제되었습니다.\n");
							break;
						case 5:
							selectMenu2_5();
							break;
						case 6:
							selectMenu2_6();
							break;
						case 7:
							selectMenu2_7();
							break;
						case 8:
							selectMenu2_8();
							break;

						default:
							System.out.println("잘못된 메뉴입니다.");
					}
					break;

				default:
					System.out.println("잘못된 입력입니다. 0~2번 중에서 선택해주세요.\n");
					break;
			}
			// 예약 테스트 후 전체 주문 + 결제 출력
			// printAllOrdersWithPayment();

		}

	}

}
