# Giới thiệu dự án Smart Dict

<div style="text-align: justify;">
Trong bối cảnh toàn cầu hóa và hội nhập quốc tế ngày càng sâu rộng, tiếng Anh đã trở thành một ngôn ngữ thiết yếu trong nhiều lĩnh vực của đời sống, từ học tập, công việc đến giao tiếp và giải trí. Nhu cầu học và sử dụng tiếng Anh ngày càng gia tăng ở mọi lứa tuổi và trình độ. Điều này tạo ra một thị trường tiềm năng lớn cho các công cụ và ứng dụng hỗ trợ học tiếng Anh hiệu quả.
	
Hiện nay, trên các nền tảng di động, đặc biệt là Android, có rất nhiều ứng dụng từ điển tiếng Anh khác nhau. Các ứng dụng này cung cấp đa dạng các tính năng như tra cứu từ vựng, phát âm, dịch thuật, học từ mới qua flashcards, bài tập, và thậm chí tích hợp cả trí tuệ nhân tạo để hỗ trợ người dùng. Tuy nhiên, bên cạnh những ưu điểm, một số ứng dụng vẫn còn tồn tại những hạn chế như:

+ Giao diện người dùng: Một số ứng dụng có giao diện phức tạp, khó sử dụng, 	đặc biệt đối với người mới bắt đầu.
 
+ Dữ liệu từ vựng: Độ chính xác và đầy đủ của dữ liệu từ vựng có thể khác nhau giữa các ứng dụng. Một số ứng dụng có thể thiếu các từ chuyên ngành hoặc các thành ngữ thông dụng.
 
+ Tính năng học tập: Các tính năng hỗ trợ học tập như luyện tập từ vựng, ngữ pháp đôi khi còn đơn giản và chưa đủ hấp dẫn người dùng.
 
+ Hiệu suất: Một số ứng dụng có thể hoạt động chậm, tốn nhiều tài nguyên của thiết bị hoặc yêu cầu kết nối internet liên tục.
 
+ Quảng cáo: Sự xuất hiện quá nhiều quảng cáo có thể gây khó chịu và gián đoạn trải nghiệm người dùng.
  
Với những phân tích về nhu cầu và thực trạng trên, việc phát triển một ứng dụng từ điển tiếng Anh mới trên nền tảng Android là hoàn toàn cần thiết. Ứng dụng này hướng đến mục tiêu cung cấp một công cụ tra cứu mạnh mẽ, chính xác, đồng thời tích hợp các tính năng học tập thông minh, giao diện thân thiện và trải nghiệm người dùng tối ưu.

# Công nghệ sử dụng

**(1) Spring Boot (API Backend):**

		Framework Java giúp xây dựng API nhanh chóng và mạnh mẽ cho ứng dụng.
  
		Ưu điểm: Phát triển nhanh, hệ sinh thái lớn, ổn định, dễ mở rộng để xử lý logic tra cứu và giao tiếp với database.
  
**(2) Android studio:**

		IDE chính thức để phát triển giao diện và logic ứng dụng Android.
  
		Là công cụ chuyên biệt, thiết kế giao diện trực quan, dễ dàng gọi và hiển thị dữ liệu từ API.
  
**(3) MySQL:**

		Hệ quản trị cơ sở dữ liệu quan hệ để lưu trữ dữ liệu từ vựng.
  
		Ưu điểm: Ổn định, hiệu suất tốt cho việc lưu trữ và truy xuất dữ liệu lớn.
  
**(4) Tương tác:**

    		Ứng dụng Android (Android Studio) gọi API (Spring Boot) để tra cứu.
      
    		API (Spring Boot) tương tác với Database (MySQL) để lấy dữ liệu.
      
    		Dữ liệu được trả về Android để hiển thị cho người dùng.

# Các bước cài đặt dự án

 **CLone dự án**

 	Đường dẫn dự án: git clone https://github.com/TuanThanhIT/LTDD_ProjectCK_SmartDict

  **Tạo database SmartDict**

   	Trong MySQL: CREATE DATABASE SmartDict;
 
 **Đường dẫn API của dự án**

  	http://localhost:8077/
   
