package kr.or.kosta.ams.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import kr.or.kosta.ams.model.Account;
import kr.or.kosta.ams.model.AccountDao;
import kr.or.kosta.ams.model.AccountManager;
import kr.or.kosta.ams.model.MinusAccount;

/**
 * 계좌 관리 프로그램 메인 화면 
 * GridBagLayout 예제
 *
 * @author 최명승
 *
 */

public class MainFrame extends JFrame {

	/** 계좌 관리자 */
//	AccountManager manager;
	AccountDao accountDao;
	
	JTable accountTable;
	AccountModel accountModel;

	JButton buttonCheck, buttonDelete, buttonSearch, buttonNew, buttonAll, buttonNull;
	JLabel labelType, labelNum, labelHolder, labelpassword, labelDeposit, labelLoan, labelList, labelCurrency, labelNull;
	JComboBox<String> choiceAccountType;
	JTextField tfAccountNum, tfAccountHolder, tfDeposit, tfLoan;
	JPasswordField tfPassword;
	
	JScrollPane viewPort;
	GridBagLayout gridLayout;
	GridBagConstraints constraints;

	String accountNum, accountOwner, deposit, restMoney2, restMoney4, borrowMoney2, borrowedMoney;
	char[] passwd1;
	long restMoney3, borrowMoney1;

	
	/** 생성자 설정 */
	public MainFrame() {
		this("KOSTA AMS - 메인화면");
	}

	public MainFrame(String title) {
		super(title);

		gridLayout = new GridBagLayout();
		constraints = new GridBagConstraints();
		
		buttonCheck = new JButton("조 회");
		buttonDelete = new JButton("삭 제");
		buttonSearch = new JButton("검 색");
		buttonNew = new JButton("신규등록");
		buttonAll = new JButton("전체조회");

		labelType = new JLabel("계좌종류");
		labelNum = new JLabel("계좌번호");
		labelHolder = new JLabel("예금주명");
		labelpassword = new JLabel("비밀번호");
		labelDeposit = new JLabel("입금금액");
		labelLoan = new JLabel("대출금액");
		labelList = new JLabel("계좌목록");
		labelCurrency = new JLabel("(단위 : 원)");
		labelNull = new JLabel();

		choiceAccountType = new JComboBox<String>();
		choiceAccountType.addItem("전체");
		choiceAccountType.addItem("입출금 계좌");
		choiceAccountType.addItem("마이너스 계좌");

		tfAccountNum = new JTextField();
		tfAccountHolder = new JTextField();
		tfPassword = new JPasswordField();
		tfDeposit = new JTextField();
		tfLoan = new JTextField();

		accountModel = new AccountModel();
		accountTable = new JTable();
		accountTable.setModel(accountModel);
		
		try {
			accountDao = new AccountDao();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tfLoan.setEnabled(false);
		tfLoan.setEditable(false);
		
	}

	
	/**
	 * 프레임에 비주얼 컴포넌트 배치
	 */
	public void setContents() {
		setLayout(gridLayout);
		
		
		constraints.fill = GridBagConstraints.BOTH;
		setLayout(gridLayout);

		
		constraints.insets = new Insets(5, 5, 5, 5);
		
		add(new JScrollPane(accountTable), 0, 6, 20, 10, 0, 0.1);
		
		add(labelType, 0, 0, 3, 1, 0, 0);
		add(labelNum, 0, 1, 3, 1, 0, 0);
		add(labelHolder, 0, 2, 3, 1, 0, 0);
		add(labelpassword, 0, 3, 3, 1, 0, 0);
		add(labelDeposit, 8, 3, 3, 1, 0, 0);
		add(labelLoan, 0, 4, 3, 1, 0, 0);
		add(labelList, 0, 5, 3, 1, 0, 0);
		add(labelCurrency, 18, 5, 2, 1, 0, 0);

		add(choiceAccountType, 3, 0, 4, 1, 0, 0);

		add(tfAccountNum, 3, 1, 5, 1, 1, 0);
		add(tfAccountHolder, 3, 2, 5, 1, 0, 0);
		add(tfPassword, 3, 3, 5, 1, 0, 0);
		add(tfDeposit, 11, 3, 9, 1, 0, 0);
		add(tfLoan, 3, 4, 5, 1, 0, 0);

		add(buttonCheck, 8, 1, 3, 1, 0, 0);
		add(buttonDelete, 11, 1, 1, 1, 0, 0);
		add(buttonSearch, 8, 2, 3, 1, 0, 0);
		add(buttonNew, 8, 4, 4, 1, 0, 0);
		add(buttonAll, 12, 4, 5, 1, 1, 0);

	}
	

	/** 비주얼 컴포넌트 배치 설정 */
	public void add(Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx,
			double weighty) {

		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		gridLayout.setConstraints(component, constraints);
		add(component);
	}

	
	/** 윈도우 종료 */
	private void exit() {
		setVisible(false);
		dispose();
		System.exit(0);
	}


	/** 마이너스 계좌 선택시 대출금액 활성화 */
	public void selectType() {
		tfLoan.setText("");
		tfDeposit.setText(" ");
		tfDeposit.setText("");
		if (choiceAccountType.getSelectedItem().equals("입출금 계좌") || choiceAccountType.getSelectedItem().equals("전체")) {
			tfLoan.setEnabled(false);
			tfLoan.setEditable(false);
		} else {
			tfLoan.setEnabled(true);
			tfLoan.setEditable(true);
			return;
		}

	}
	

	/** 계좌 신규 등록 */
	public void addAccount() {
		
		accountNum = tfAccountNum.getText();
		accountOwner = tfAccountHolder.getText();
		passwd1 = tfPassword.getPassword();		
		deposit = tfDeposit.getText();
		borrowedMoney = tfLoan.getText();

		String password2 =String.valueOf(passwd1);
		int passwd2 = Integer.parseInt(password2);
		long restMoney2 = Long.parseLong(deposit);
		

		if (choiceAccountType.getSelectedItem().equals("입출금 계좌")) {
			try {
				accountDao.createAccount(new Account(accountNum, accountOwner, passwd2, restMoney2));
			} catch (IOException e) {
				e.printStackTrace();
			}
			showEventMessage("계좌가 생성되었습니다.");
		} 
		else if(choiceAccountType.getSelectedItem().equals("마이너스 계좌")) {
			long borrowedMoney2 = Long.parseLong(borrowedMoney);
			try {
				accountDao.createAccount(new MinusAccount(accountNum, accountOwner, passwd2, restMoney2, borrowedMoney2));
			} catch (IOException e) {
				e.printStackTrace();
			}
			showEventMessage("계좌가 생성되었습니다.");
		} else if(choiceAccountType.getSelectedItem().equals("전체")) {
			showEventMessage("생성하실 계좌 유형을 선택해 주세요.");
		}
		return;
	}
	

	/** 계좌 조회 */
	public void getAccount() {
		accountNum = tfAccountNum.getText();
		// 유효성 검증
		if (isEmpty(accountNum)) {
			showEventMessage("올바른 계좌번호를 입력해 주세요.");
			return;
		}
		Account searchedAccount;
		try {
			searchedAccount = accountDao.checkWithAccountNum(accountNum);
			if (searchedAccount != null) {
				accountModel.addAccount(searchedAccount);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	
	/** 전체 계좌 조회 */
	public void listAll() {
		List<Account> list;
		try {
			list = accountDao.listAll();
			accountModel.addAccountList(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/** 예금주명으로 계좌 검색 */
	public void searchAccount() {
		accountOwner = tfAccountHolder.getText();
		Account searchedList;
		try {
			searchedList = accountDao.checkWithAccountOwner(accountOwner);
			if (searchedList != null) {
				accountModel.addAccount(searchedList);
			} 
		} catch (IOException e) {
			showEventMessage("계좌를 찾을 수 없습니다. 입력 정보를 확인해 주세요.");
			e.printStackTrace();
		}
		
		
	}

	
	/** 계좌 삭제 */
	public void removeAccount() {
		try {
			accountNum = tfAccountNum.getText();
			if (accountDao.deleteWithAccountNum(accountNum) == true) {
				showEventMessage("요청하신 계좌가 삭제되었습니다.");
				return;
			}
			showEventMessage("계좌 삭제에 실패하였습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/** 유효성 검사 메소드 */
	private boolean isEmpty(String string) {
		if (string == null || string.trim().length() == 0) {
			return true;
		}
		return false;
	}
	
	/** 알림 메시지 호출 */
	public void showEventMessage(String inputMessage) {
		JOptionPane.showMessageDialog(this, inputMessage, "알림", JOptionPane.ERROR_MESSAGE);
	}
	

	/** 이벤트 소스에 이벤트 처리 */
	public void eventRegist() {

		/** 예금주로 계좌 검색 이벤트 처리 */
		buttonSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				searchAccount();
			}
		});

		/** 계좌 삭제 이벤트 처리 */
		buttonDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				removeAccount();
			}
		});

		/** 전체 계좌 조회 이벤트 처리 */
		buttonAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listAll();
			}
		});

		/** 계좌 조회 이벤트 처리 */
		buttonCheck.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getAccount();
			}
		});

		/** 계좌 신규 생성 이벤트 처리 */
		buttonNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addAccount();
			}
		});

		/** 마이너스 계좌 선택시 이벤트 처리 */
		choiceAccountType.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					selectType();
				}
			}
		});

		/** 윈도우 종료 이벤트 처리 */
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
	}

}
