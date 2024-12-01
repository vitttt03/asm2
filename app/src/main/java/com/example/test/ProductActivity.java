//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.test.CartActivity;
//import com.example.test.R;
//
//public class ProductActivity extends AppCompatActivity {
//
//    private Button btnAddToCart;
//    private TextView tvProductName, tvProductPrice;
//    private int productPrice = 100000;  // Giá của sản phẩm
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product);
//
//        // Khởi tạo các view
//        tvProductName = findViewById(R.id.tv_product_name);
//        tvProductPrice = findViewById(R.id.tv_product_price);
//        btnAddToCart = findViewById(R.id.btn_add_to_cart);
//
//        // Sự kiện khi nhấn vào nút "Mua"
//        btnAddToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Gửi dữ liệu (tên sản phẩm và giá) sang Activity giỏ hàng
//                Intent intent = new Intent(ProductActivity.this, CartActivity.class);
//                intent.putExtra("product_name", tvProductName.getText().toString());
//                intent.putExtra("product_price", productPrice);
//                startActivity(intent);
//            }
//        });
//    }
//}
