package ec;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.BuyDataBeans;
import beans.DeliveryMethodDataBeans;
import beans.ItemDataBeans;
import dao.DeliveryMethodDAO;

/**
 * 購入商品確認画面
 * @author d-yamaguchi
 *
 */
@WebServlet("/BuyConfirm")
public class BuyConfirm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			//選択された配送方法IDを取得
			int inputDeliveryMethodId = Integer.parseInt(request.getParameter("delivery_method_id"));


			//選択されたIDをもとに配送方法Beansを取得

			DeliveryMethodDataBeans userSelectDMB = DeliveryMethodDAO.getDeliveryMethodDataBeansByID(inputDeliveryMethodId);

			System.out.println("ID:" + userSelectDMB.getId() +  "プラン:" + userSelectDMB.getName() +  "料金：" + userSelectDMB.getPrice());

			//買い物かご
			ArrayList<ItemDataBeans> cartIDBList = (ArrayList<ItemDataBeans>) session.getAttribute("cart"); //セッションのカゴデータ呼び出し
			//合計金額
			int totalPrice = EcHelper.getTotalItemPrice(cartIDBList); //カートの中身全部足す　あくまで小計


			BuyDataBeans bdb = new BuyDataBeans();

			//BuyDataBeansのフィールド　delivertMethodId　deliveryMethodPriceをjspで表示させてる
			//そもそもセットされてなくない？
//			private int id;
//			private int userId;
//			private int totalPrice;
//			private int delivertMethodId;
//			private Date buyDate;
//			private String deliveryMethodName;
//			private int deliveryMethodPrice;



			bdb.setUserId((int) session.getAttribute("userId"));
			bdb.setDelivertMethodId(userSelectDMB.getId());
			bdb.setTotalPrice(totalPrice+userSelectDMB.getPrice());

			//課題1追加
			bdb.setDeliveryMethodName(userSelectDMB.getName());
			bdb.setDeliveryMethodPrice(userSelectDMB.getPrice());

			//課題1変更　小計＋配送費
			bdb.setTotalPrice(totalPrice+userSelectDMB.getPrice());



			//購入確定で利用
			session.setAttribute("bdb", bdb);
			request.getRequestDispatcher(EcHelper.BUY_CONFIRM_PAGE).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("errorMessage", e.toString());
			response.sendRedirect("Error");
		}
	}

}
