package kr.or.kosta.ams.model;

public class MinusAccount extends Account {

	private long borrowMoney;

	public MinusAccount() {
		this("1111-1111-2222-3333", "홍길동", 1111, 0, 0);
	}

	public MinusAccount(String accountNum, String accountOwner, int passwd, long restMoney, long borrowMoney) {
		super(accountNum, accountOwner, passwd, restMoney);
		this.borrowMoney = borrowMoney;
	}

	public long getBorroMoney() {
		return borrowMoney;
	}

	public void setBorroMoney(long borroMoney) {
		this.borrowMoney = borroMoney;
	}
	
	
	@Override
	public long getRestMoney() {
		return super.getRestMoney() - borrowMoney;
	}
	
	@Override
	public String print() {
		return getAccoutNum() + "\t" + getAccoutOwner() + "\t" + "****" + "\t" + super.getRestMoney() + "\t" + getBorroMoney();
	}

	@Override
	public String toString() {
		return "마이너스" + "   " + getAccoutNum() + "      "  + getAccoutOwner() + "        " + "-" + getRestMoney() + "           " + getBorroMoney();
	}
	
	
	
	
}
