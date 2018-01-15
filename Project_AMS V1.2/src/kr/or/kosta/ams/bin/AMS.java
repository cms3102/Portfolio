/*
 * Copyright (c) 2017, KOSTA. All rights reserved.
 * KOSTA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package kr.or.kosta.ams.bin;

import kr.or.kosta.ams.util.GUIUtil;
import kr.or.kosta.ams.view.MainFrame;

/**
 * KOSTA 은행 계좌 관리 애플리케이션
 *
 * @author 최명승
 *
 */
public class AMS {

	public static void main(String[] args) {
		
		MainFrame frame = new MainFrame();
		frame.setContents();
		frame.pack();
//		frame.setSize(500, 600);
		GUIUtil.setCenterScreen(frame);
		GUIUtil.setLookNFeel(frame, GUIUtil.STYLE_NIMBUS);
		frame.eventRegist();
		frame.setVisible(true);
		
	}

}
