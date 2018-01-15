package kr.or.kosta.ams.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * 계좌 정보 관리
 *
 * @author 최명승
 *
 */
public class AccountDao extends Account {

	/** 파일 저장 경로 */
	private static final String FILE_PATH = "accounts.dbf";

	/** 계좌 수 저장을 위한 컬럼 사이즈 설정 */
	private static final int RECORD_COUNT_LENGTH = 4;

	/** 계좌 정보 저장을 위한 컬럼 사이즈 설정 */
	private static final int ACCOUNTNUM = 40;
	private static final int ACCOUNTOWNER = 10;
	private static final int PASSWORD = 4;
	private static final int RESTMONEY = 8;
	private static final int BORROWEDMONEY = 8;

	/** 계좌 정보 저장을 위해 필요한 레코드 사이즈 */
	public static final int RECOROD_LENGTH = ACCOUNTNUM + ACCOUNTOWNER + PASSWORD + RESTMONEY + BORROWEDMONEY;

	private RandomAccessFile file;

	/** 저장된 계좌 수 */
	private int recordCount = 0;

	public AccountDao() throws IOException {

		file = new RandomAccessFile(FILE_PATH, "rw");

		if (file.length() != 0) {
			recordCount = file.readInt();
		} else {
			System.out.println("저장된 계좌가 없습니다.");
		}
	}

	public int getRecordCount() {
		return recordCount;
	}

	/**
	 * 계좌 정보 저장(계좌 개설)
	 * @param account
	 * @throws IOException
	 */
	public void createAccount(Account account) throws IOException {
		// 파일 내 저장 가능한 레코드 위치로 포인터 이동
		file.seek((recordCount * RECOROD_LENGTH) + RECORD_COUNT_LENGTH);

		// 받은 매개변수에서 저장할 정보 추출하여 변수 처리
		String accountNum = account.getAccoutNum();
		String accountOwner = account.getAccoutOwner();
		int password = account.getPasswd();
		long restMoney = account.getRestMoney();
		long borowedMoney = 0;
		if (account instanceof MinusAccount) {
			borowedMoney = ((MinusAccount) account).getBorroMoney();
		}

		// 계좌번호 저장
		int lengthNum = accountNum.length();
		for (int i = 0; i < ACCOUNTNUM / 2; i++) {
			file.writeChar(i < lengthNum ? accountNum.charAt(i) : ' ');
		}

		// 예금주명 저장
		int lengthOwner = accountOwner.length();
		for (int i = 0; i < ACCOUNTOWNER / 2; i++) {
			file.writeChar(i < lengthOwner ? accountOwner.charAt(i) : ' ');
		}

		// 비밀번호 저장
		file.writeInt(password);

		// 잔액 저장
		file.writeLong(restMoney);

		// 대출 금액 저장
		file.writeLong(borowedMoney);

		// 포인터 위치 초기화 및 저장 계좌 수 업데이트
		file.seek(0);
		file.writeInt(++recordCount);
	}

	/**
	 * 전체 계좌 리스트 반환
	 * @return 전체 계좌의 정보가 담긴 List 반환
	 * @throws IOException
	 */
	public List<Account> listAll() throws IOException {
		List<Account> list = new ArrayList<>();
		for (int i = 0; i < recordCount; i++) {
			list.add(loadAccountInfo(i));
		}
		return list;
	}

	/**
	 * 전체 계좌 정보 조회 
	 * @param index
	 * @return 전체 계좌 출력에 필요한 계좌 정보 리턴
	 * @throws IOException
	 */
	private Account loadAccountInfo(int index) throws IOException {

		/** 계좌 번호 조회 */
		String accountNum = "";
		file.seek((index * RECOROD_LENGTH) + RECORD_COUNT_LENGTH);
		for (int i = 0; i < ACCOUNTNUM / 2; i++) {
			accountNum += file.readChar();
		}
		accountNum = accountNum.trim();

		/** 예금주명 조회 */
		String accountOwner = "";
		for (int i = 0; i < ACCOUNTOWNER / 2; i++) {
			accountOwner += file.readChar();
		}
		accountOwner = accountOwner.trim();
		/** 비밀번호 조회 */
		int password;
		password = file.readInt();

		/** 잔액 조회 */
		long restMoney;
		restMoney = file.readLong();

		/** 대출금액 조회 */
		long borrowedMoney;
		borrowedMoney = file.readLong();
		Account account;
		if (borrowedMoney > 0) {
			account = new MinusAccount(accountNum, accountOwner, password, restMoney + borrowedMoney, borrowedMoney);
		} else {
			account = new Account(accountNum, accountOwner, password, restMoney);
		}

		return account;
	}


	/**
	 * 계좌번호로 계좌 조회
	 * @param accountNum
	 * @return 입력된 계좌번호가 포함된 계좌 정보 반환
	 * @throws IOException
	 */
	public Account checkWithAccountNum(String accountNum) throws IOException {
		List<Account> fullList = listAll();
		Account account;
		for (int i = 0; i < fullList.size(); i++) {
			if (fullList.get(i).getAccoutNum().equals(accountNum)) {
				account = fullList.get(i);
				return account;
			}
		}
		return null;
	}

	
	/**
	 * 예금주명으로 계좌 조회
	 * @param accountOwner
	 * @return 입력된 예금주명이 포함된 계좌 정보 반환
	 * @throws IOException
	 */
	public Account checkWithAccountOwner(String accountOwner) throws IOException {
		List<Account> fullList = listAll();
		Account account;
		for (int i = 0; i < fullList.size(); i++) {
			if (fullList.get(i).getAccoutOwner().equals(accountOwner)) {
				account = fullList.get(i);
				return account;
			}
		}
		return null;
	}

	
	/**
	 * 계좌 삭제
	 * @param accountNum
	 * @return 입력된 계좌번호로 계좌 정보 삭제 성공 여부 반환
	 * @throws IOException
	 */
	public boolean deleteWithAccountNum(String accountNum) throws IOException {
		List<Account> fullList = listAll();
		for (int i = 0; i < fullList.size(); i++) {
			if (fullList.get(i).getAccoutNum().equals(accountNum)) {
				fullList.remove(fullList.get(i));
			}
		}
		recordCount = 0;
		if (fullList.size() > 0) {
			for (int j = 0; j < fullList.size(); j++) {
				Account account = fullList.get(j);
				createAccount(account);
			}
		}else {
			for (int i = 0; i < 100; i++) {
				file.seek(i);
				file.writeChar(' ');
			}
		}	
		return true;				
	}


	/** 스트림 닫기 */
	public void close() {
		if (file != null) {
			try {
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// public static void main(String[] args) throws IOException {
	// AccountDao dao = new AccountDao();
	//// dao.createAccount(new Account("1111-2222", "김기정", 1111, 10000));
	// List<Account> list = dao.listAll();
	// for (Account account : list) {
	// System.out.println(account);
	// }
	//
	//
	// }

}
