package kr.or.kosta.ams.model;
/**
 * 배열을 이용하여 계좌 관리(개설, 조회, 검색, 삭제)
 * @author 최명승
 *
 */
public class AccountManager {
	private Account[] accounts; // Account[]라는 배열(클래스)과 accounts라는 변수
	private int count; // 자동으로 0으로 초기화 되니까 따로 초기화 안해도 됨
	
	public AccountManager() {
		this(100);
	}

	public AccountManager(int size) {
		this.accounts = new Account[size];
	}
	
	public Account[] getAccounts() {
		return accounts;
	}
	
	public int getCount() {
		return count;
	}


	/** 계좌 개설 */
	
	public void open(Account account) {
		accounts[count] = account;
		count++;
	}
	
	public void open(String accountNum, String accountOwner, int passwd, long restMoney) {
//		accounts[count] = new Account(accountNum, accountOwner, passwd, restMoney);
//		count++;
		
		
//		??? new Account(accountNum, accountOwner, passwd, restMoney);
//		openAccount(acc);
		open(new Account(accountNum, accountOwner, passwd, restMoney));
	}
	
	
	
	/** 전체 계좌 반환 */
	
	public Account[] listAll() {
		if (count == 0) return null;
		Account[] list = new Account[count];
		for (int i = 0; i < count; i++) {
			list[i] = accounts[i];
		}
		return list;
	}
	
	
	/** 계좌번호로 계좌 조회 */
	public Account get(String accountNum) {
		for (int i = 0; i < count; i++) {
			if (accounts[i].getAccoutNum().equals(accountNum)) {
				return accounts[i];
				
			}
			
		}
		
		return null;
	}

	
	/** 예금주명으로 계좌 검색 */
	public Account[] search(String accountOwner) {
		Account[] searchAccounts = null;
		// 검색 계좌수
		int searchCount = searchCount(accountOwner);
		//검색된 계좌 존재 시
		if(searchCount != 0){
			searchAccounts = new Account[searchCount];
			int position = 0;
			for(int i=0; i<count; i++){
				if (accounts[i].getAccoutOwner().equals(accountOwner)){
					searchAccounts[position] = accounts[i];
					position++;
				}
			}
		}
		return searchAccounts;
	}
	
	/** 예금주명으로 검색된 계좌 개수 반환 */
	public int searchCount(String accountOwner){
		int searchCount = 0;
		for(int i=0; i<count; i++){
			if (accounts[i].getAccoutOwner().equals(accountOwner)){
				searchCount++;
			}
		}
		return searchCount;
	}
	
	/** 계좌번호로 계좌 삭제 */
	public boolean remove(String accountNum) {
		for(int i=0; i<count; i++){
			if (accounts[i].getAccoutNum().equals(accountNum)){
				for (int j=i; j<count-1; j++){
					accounts[j] = accounts[j+1];
				}
				count--;
				return true;
			}
		}
		return false;
	}
	
	
}
