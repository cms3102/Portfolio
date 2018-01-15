package kr.or.kosta.ams.model;

import kr.or.kosta.ams.view.MainFrame;

/**
 * 은행 계좌 추상화
 */
public class Account {
	// 클래스(스태틱)변수
	public static final String BANK_NAME = "Kosta은행";
	
	// 인스턴스 변수 은닉화
	private String accountNum;
	private String accountOwner;
	private int passwd;
	private long restMoney;

	int id;
	static int count = 0;

 

	// 생성자 Overloading(중복 정의)

	public Account(){
		this("0000", "익명", 0, 0 );
	}

	public Account(String accountNum, String accountOwner){
		this(accountNum, accountOwner, 0, 0 );
	
	}

	public Account(String accountNum, String accountOwner, int passwd, long restMoney){
		this.accountNum = accountNum;
		this.accountOwner = accountOwner;
		this.passwd = passwd;
		this.restMoney = restMoney;

		id = ++count;
	
	}

	// setter / getter 메소드
	
	
	public void setAccountNum(String accountNum){
		this.accountNum = accountNum;
	}
	

	public String getAccoutNum(){
		return accountNum;
	}


	public void setAccountOwner(String accountOwner){
		this.accountOwner = accountOwner;
	}
	public String getAccoutOwner(){
		return accountOwner;
	}


	public void setPasswd(int passwd){
		this.passwd = passwd;
	}
	public int getPasswd(){
		return passwd;
	}


	public void setRestMoney(long restMoney){
		this.restMoney = restMoney;
	}
	public long getRestMoney(){
		return restMoney;
	}



	public long deposit(long money){
		restMoney += money;
		return restMoney;
	} 

	public long withdraw(long money){
		restMoney -= money;
		return restMoney;
	}

	public boolean checkPasswd(int pw){
		return passwd==pw;
	}

	public String print(){
		//System.out.print("계좌번호\t예금주명\t****\t현재잔액");
		return accountNum + "       "  + accountOwner + "          "  + getRestMoney() + "             ";
	}

	// 클래스 메소드
	public static void commonMethod(){
		System.out.println("공통기능입니다.");
	}

	@Override
	public String toString() {
		return "입출금" + "     " + accountNum + "       "  + accountOwner + "          "  + restMoney + "             ";
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Account) {
			//위임형 비교
			return toString().equals(obj.toString());
			
		}
		return false;
	}
	

	
}
