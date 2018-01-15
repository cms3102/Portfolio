package kr.or.kosta.ams.view;

import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import kr.or.kosta.ams.model.Account;
import kr.or.kosta.ams.model.MinusAccount;


public class AccountModel extends AbstractTableModel {
	
	Vector<String> headerItems;
	Vector<Account> detailItems;
	
	public AccountModel() {
		headerItems = new Vector<String>();
		headerItems.add("계좌종류");
		headerItems.add("계좌번호");
		headerItems.add("예금주명");
		headerItems.add("현재잔액");
		headerItems.add("대출금액");
		
		detailItems = new Vector<Account>();
		
	}

	@Override
	public int getRowCount() {
		return detailItems.size();
	}

	@Override
	public int getColumnCount() {
		return headerItems.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object cellItem = null;
		Account account = detailItems.elementAt(rowIndex);
		switch (columnIndex) {
		case 0:
			cellItem = account instanceof MinusAccount ?  "마이너스계좌" : "입출금계좌";
			break;
		case 1:
			cellItem = detailItems.elementAt(rowIndex).getAccoutNum();
			break;
		case 2:
			cellItem = detailItems.elementAt(rowIndex).getAccoutOwner();
			break;
		case 3:
			cellItem = detailItems.elementAt(rowIndex).getRestMoney();
			break;
		case 4:
			if(account instanceof MinusAccount) {
				MinusAccount ma = (MinusAccount)account;
				cellItem = ma.getBorroMoney();
			}else {
				cellItem = 0;
			}
			break;
		}
		return cellItem;
	}
	
	@Override
	public String getColumnName(int column) {
		return headerItems.elementAt(column);
	}
	
	
	public void addAccount(Account account) {
		detailItems.clear();
		detailItems.addElement(account);
//		VEIW에 변경 사항 통보
		fireTableStructureChanged();
	}
	
	
	public void addAccountList(List<Account> accounts) {
		detailItems.clear();
		for (Account account : accounts) {
			detailItems.addElement(account);
		}
		fireTableStructureChanged();
	}

}
