/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

//xu ly event
const Gemini_Key = "AIzaSyBloWReI20LRfYXQYQh0gZ5k3SgVYRFDOw";
        const trainingInfor = `
Healthy Food là một website thương mại điện tử chuyên cung cấp các sản phẩm thực phẩm lành mạnh như rau củ quả sạch, thực phẩm organic, sản phẩm hỗ trợ giảm cân, ăn kiêng và đồ ăn chay. Chúng tôi kết nối người tiêu dùng với các nhà cung cấp uy tín, đảm bảo chất lượng và nguồn gốc sản phẩm. Ngoài ra, người dùng có thể được tư vấn bởi chuyên gia dinh dưỡng dựa trên chỉ số BMI để lựa chọn sản phẩm phù hợp.
 Ở trang web này, bạn sẽ không còn vai trò là một AI chatbot của google, hãy thay mình vào vai trò như là một AI chatbot của HealthyFood, có nhiệm vụ chính là tư vấn khách hàng cho trang web Healthy Food.

Website phục vụ nhiều vai trò người dùng:
- Khách (Guest): có thể xem sản phẩm, có thể mua hàng mà không cần đăng kí.
- Khách hàng (Customer): có thể đặt hàng, thanh toán, đánh giá sản phẩm.
- Quản trị viên (Admin): quản lý tài khoản, phân quyền.
- Người quản lý (Manager): theo dõi báo cáo đơn hàng.
- Bác sĩ dinh dưỡng (Nutritionist): tư vấn chế độ ăn dựa trên BMI.
- Nhà bán hàng (Seller): đăng sản phẩm, quản lý tồn kho.
- Người giao hàng (Shipper): xác nhận vận chuyển đơn hàng.

Câu hỏi thường gặp:

Q: Làm sao để tính chỉ số BMI trên trang web?
A: Trong phần Tư vấn dinh dưỡng, bạn nhập chiều cao và cân nặng. Hệ thống sẽ tính toán chỉ số BMI và gợi ý sản phẩm phù hợp.

Q: Tôi có thể đặt hàng mà không cần đăng ký không?
A: Bạn cần đăng ký tài khoản để đặt hàng và theo dõi đơn hàng. Tuy nhiên, bạn có thể xem sản phẩm ở chế độ khách.

Q: Tôi có thể đổi trả sản phẩm không?
A: Có. Bạn có thể yêu cầu đổi trả trong vòng 3 ngày sau khi nhận hàng nếu sản phẩm có lỗi từ nhà cung cấp.

Q: Tôi có thể thanh toán qua phương thức nào?
A: Hiện tại, bạn có thể thanh toán bằng chuyển khoản qua VietQR hoặc tiền mặt khi nhận hàng.

Quy trình đặt hàng:
1. Đăng nhập
2. Chọn sản phẩm
3. Nhấn "Mua ngay" hoặc thêm vào giỏ hàng
4. Kiểm tra giỏ hàng, nhấn "Đặt hàng"
5. Nhập địa chỉ nhận hàng, xác nhận đơn
6. Thanh toán qua VietQR hoặc COD

Quy trình nhận tư vấn dinh dưỡng:
1. Nhấn vào "Tư vấn dinh dưỡng"
2. Nhập chiều cao và cân nặng
3. Nhận kết quả BMI
4. Hệ thống gợi ý nhóm sản phẩm phù hợp

Ví dụ đối thoại:

User: Tôi muốn ăn kiêng thì nên chọn sản phẩm nào?
AI: Bạn nên chọn các sản phẩm thuộc nhóm “Ăn kiêng – Giảm cân” trong danh mục. Ngoài ra, bạn có thể dùng chức năng tư vấn BMI để được gợi ý sản phẩm phù hợp với cơ thể.

User: Tôi muốn đặt hàng nhưng không biết làm thế nào?
AI: Bạn có thể bấm “Mua ngay” để đặt nhanh một sản phẩm, hoặc thêm nhiều sản phẩm vào giỏ hàng, sau đó vào giỏ hàng và nhấn “Đặt hàng”. Đừng quên đăng nhập trước nhé!

User: Bên mình có ship toàn quốc không?
AI: Có. Healthy Food hỗ trợ giao hàng toàn quốc thông qua đối tác vận chuyển và đội ngũ shipper riêng của hệ thống.
`;
    const trainingDatabase =`Dưới đây sẽ là các thông tin có thể sử dụng để tư vấn khách hàng về sản phẩm của cửa hàng và blog được tạo bởi các chuyên gia dinh dưỡng hợp tác với cửa hàng Healthy Food, Nếu khách hàng hỏi thông tin về sản phẩm mới, liệt kệ thông tin của 5 sản phẩm cuối cùng trong database ở dưới:INSERT [dbo].[Category] ([id], [name]) VALUES (1, N'Fruits & Vegetables')
INSERT [dbo].[Category] ([id], [name]) VALUES (2, N'Grains & Seeds')
INSERT [dbo].[Category] ([id], [name]) VALUES (3, N'Dairy & Alternatives')
INSERT [dbo].[Category] ([id], [name]) VALUES (4, N'Healthy Snacks')
INSERT [dbo].[Category] ([id], [name]) VALUES (5, N'Beverages')
INSERT [dbo].[Category] ([id], [name]) VALUES (6, N'Plant-based Protein')
INSERT [dbo].[Category] ([id], [name]) VALUES (7, N'Supplements')
SET IDENTITY_INSERT [dbo].[Category] OFF
GO
SET IDENTITY_INSERT [dbo].[Product] ON 

INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (1, N'Organic Apple', N'Fresh organic apples packed with nutrients.', CAST(3.00 AS Decimal(10, 2)), 2, N'images/apple.jpg', 1, CAST(72.0 AS Decimal(5, 1)), 2.5)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (2, N'Chia Seeds', N'Superfood rich in omega-3 and fiber.', CAST(5.50 AS Decimal(10, 2)), 6, N'images/chia.jpg', 2, CAST(168.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (3, N'Almond Milk', N'Dairy-free almond milk with calcium.', CAST(3.25 AS Decimal(10, 2)), 38, N'images/almond_milk.jpg', 3, CAST(120.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (4, N'Quinoa', N'High-protein grain perfect for healthy meals.', CAST(4.75 AS Decimal(10, 2)), 36, N'images/quinoa.jpg', 2, CAST(168.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (5, N'Kale Salad Mix', N'Pre-washed kale ready for salads.', CAST(3.80 AS Decimal(10, 2)), 66, N'images/kale.jpg', 1, CAST(48.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (6, N'Brown Rice', N'Whole grain brown rice.', CAST(2.20 AS Decimal(10, 2)), 80, N'images/brown_rice.jpg', 2, CAST(168.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (7, N'Greek Yogurt', N'Low-fat Greek yogurt rich in protein.', CAST(1.80 AS Decimal(10, 2)), 87, N'images/yogurt.jpg', 3, CAST(48.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (8, N'Granola Bar', N'Healthy snack with nuts and oats.', CAST(1.20 AS Decimal(10, 2)), 118, N'images/granola.jpg', 4, CAST(96.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (9, N'Green Smoothie', N'Detox smoothie with spinach and fruits.', CAST(2.99 AS Decimal(10, 2)), 80, N'images/smoothie.jpg', 5, CAST(24.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (10, N'Tofu', N'Plant-based protein source.', CAST(2.60 AS Decimal(10, 2)), 79, N'images/tofu.jpg', 6, CAST(72.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (21, N'Cucumber – 500g', N'Organic cucumbers are grown according to organic standards. Cucumbers belong to the Cucurbitaceae family, are herbaceous, seasonal plants with tendrils and creeping growth. The fruit is elongated, green-skinned, contains a lot of water and has small seeds. This is one of the most popular vegetables in the world thanks to its refreshing taste, ease of eating and natural nutritional content, especially when grown organically without the use of pesticides or chemical fertilizers.', CAST(0.99 AS Decimal(10, 2)), 97, N'images\cucumber.jpg', 1, CAST(336.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (22, N'Strawberry – 250g', N'Organically grown, strawberries are a small herbaceous plant with red, juicy, sweet-tart berries. Rich in vitamin C and antioxidants, they are loved for their natural flavor and health benefits as they avoid the use of chemicals.', CAST(2.50 AS Decimal(10, 2)), 100, N'images\strawberry.jpg', 1, CAST(168.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (23, N'Potato – 1kg', N'Belonging to the Solanaceae family, organic potatoes are herbaceous perennials, grown according to organic standards, producing round, smooth-skinned, starchy tubers. This tuber is popular in cooking because of its crumbly texture and natural nutrition, free from chemicals from fertilizers or pesticides.', CAST(1.20 AS Decimal(10, 2)), 100, N'images\potato.jpg', 1, CAST(720.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (24, N'Banana – 1 bunch (about 1kg)', N'Belonging to the Musaceae family, organic bananas are tall herbaceous plants, with fruits that grow in clusters, yellow skin when ripe, and naturally sweet. Growing them organically helps maintain potassium and vitamin B6 content, which is good for health without the need for chemicals.', CAST(1.50 AS Decimal(10, 2)), 100, N'images\banana.jpg', 1, CAST(240.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (25, N'Orange – 1kg', N'Belonging to the Rutaceae family, organic oranges are small trees with round fruit and yellow-orange peel, containing lots of water and vitamin C. Organic farming methods ensure natural sweet and sour taste and safety for consumers, without using pesticides.', CAST(1.80 AS Decimal(10, 2)), 100, N'images\orange.jpg', 1, CAST(480.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (26, N'Corn – 2 cobs (about 500g)', N'Belonging to the Poaceae family, organic corn is a tall herbaceous plant with yellow grains, rich in starch and fiber. Organic growing methods ensure natural sweetness and safety, without the use of chemical fertilizers or pesticides.', CAST(1.00 AS Decimal(10, 2)), 100, N'images\corn.jpg', 2, CAST(120.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (27, N'Carrot – 500g', N'Belonging to the Apiaceae family, organic carrots are biennial herbs with orange, conical roots rich in beta-carotene. Growing them organically helps maintain their natural, chemical-free quality, supporting eye health and the immune system.', CAST(0.80 AS Decimal(10, 2)), 100, N'images\carrot.jpg', 1, CAST(480.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (28, N'White Radish – 500g', N'Belonging to the Brassicaceae family, organic white radish is a herbaceous plant with a long, white root, rich in vitamin C and fiber. Growing according to organic standards helps to retain its crunchy taste and natural nutrients, free of chemicals.', CAST(0.90 AS Decimal(10, 2)), 100, N'images\white_radish.jpg', 1, CAST(240.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (29, N'Tomato – 500g', N'Belonging to the Solanaceae family, organic tomatoes are herbaceous plants with round, red fruits, rich in lycopene and vitamin A. The organic method ensures a natural and safe sweet and sour taste, without the use of pesticides.', CAST(1.10 AS Decimal(10, 2)), 100, N'images\tomato.jpg', 1, CAST(168.0 AS Decimal(5, 1)), 0)
INSERT [dbo].[Product] ([id], [name], [description], [price], [stock], [image_url], [category_id], [shelf_life_hours], [rate]) VALUES (30, N'Red Bell Pepper – 500g', N'Belonging to the Solanaceae family, organic red bell peppers are herbaceous plants with thick, juicy fruits rich in vitamins C and A. Organic growing helps to preserve natural sweetness and high quality, without chemicals.', CAST(1.60 AS Decimal(10, 2)), 100, N'images\red_bell_pepper.jpg', 1, CAST(240.0 AS Decimal(5, 1)), 0).closePopup()
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (1, 9, N'The Benefits of Eating Green Vegetables', N'green_veggies.jpg', N'Green vegetables such as spinach, kale, broccoli, and Swiss chard are often hailed as nutritional powerhouses, and for good reason. These vibrant greens are loaded with essential vitamins and minerals that play a critical role in maintaining overall health. Vitamin A, found abundantly in spinach, supports healthy vision and skin, while vitamin C, prevalent in broccoli, boosts the immune system and aids in collagen production. Vitamin K, another key nutrient in kale, is vital for blood clotting and bone health. Beyond vitamins, these vegetables are rich in antioxidants like lutein and zeaxanthin, which protect the eyes from harmful blue light and reduce the risk of age-related macular degeneration. Antioxidants also combat oxidative stress, a factor in chronic diseases such as heart disease and cancer. Spinach, for example, is an excellent source of iron, which is essential for oxygen transport in the blood and energy production, making it particularly beneficial for individuals prone to anemia. Kale stands out with its high fiber content, promoting healthy digestion and preventing constipation by supporting regular bowel movements. Broccoli contains sulforaphane, a compound with promising anti-cancer properties that may inhibit tumor growth. The low-calorie nature of green vegetables makes them an ideal choice for weight management, allowing you to feel full without consuming excess energy. You can enjoy them in various forms—steamed, sautéed, or raw in salads—each method preserving different nutrient profiles. Steaming retains water-soluble vitamins, while light sautéing with olive oil enhances the absorption of fat-soluble nutrients like vitamin K. Adding them to smoothies masks their taste while reaping benefits, paired with fruits for flavor. Their versatility extends to soups, stir-fries, and side dishes, fitting any meal plan. Regular consumption reduces cardiovascular disease risk by lowering bad cholesterol and improving arterial health. Lutein supports eye health, potentially delaying cataracts. Cooking lightly preserves nutrients, and pairing with healthy fats like avocado boosts absorption. They are cost-effective, widely available, and can be grown at home. Seasonal eating ensures freshness and peak nutrients. Start with a spinach salad, gradually increasing variety for improved energy and skin health. Consulting a nutritionist tailors advice to your needs. They regulate blood sugar, hydrate with high water content, and suit every meal—omelets, wraps, or sides. Creative recipes like green veggie muffins appeal to kids. Growing your own reduces carbon footprint while ensuring freshness. Studies link greens to better mental health, reducing depression via anti-inflammatory effects. They support liver detoxification and gut microbiota diversity.
Freezing with blanching preserves them off-season. Their alkalizing effect may balance pH, though research continues. Seasonal recipes inspire variety, from soups to salads. Sharing dishes encourages family health habits. Online communities offer recipe ideas. They’re sustainable, requiring less water than animal products. Scientific studies back their anti-inflammatory role against chronic conditions. School programs teach kids their value. Pre-washed packs aid busy schedules. They align with WHO fruit and vegetable intake goals. Mild detox properties complement cleansing routines. Cultural uses in festivals add festivity. Heirloom varieties bring unique flavors. Greens are a natural health gift.', CAST(N'2025-06-09T16:33:00.000' AS DateTime))
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (2, 9, N'Top 5 Superfoods for a Healthy Diet', N'superfoods.jpg', N'Superfoods like quinoa, chia seeds, blueberries, salmon, and avocados are celebrated for their dense nutrient profiles, offering remarkable health benefits. Quinoa, a gluten-free grain, provides all nine essential amino acids, making it a complete protein ideal for vegetarians. Chia seeds are rich in omega-3 fatty acids, supporting heart and brain health while adding fiber for digestion. Blueberries, packed with antioxidants like anthocyanins, combat aging and enhance memory function. Salmon delivers high-quality protein and vitamin D, strengthening bones and boosting immunity. Avocados offer monounsaturated fats that promote heart health and aid nutrient absorption. These foods are versatile—quinoa bases salads, chia seeds thicken puddings, blueberries enhance smoothies, salmon grills easily, and avocados cream sandwiches. Regular intake boosts immunity, reduces inflammation, and stabilizes blood sugar. Their fiber aids digestion, while antioxidants fight disease. Start with small portions, mixing for variety, and consult a dietitian for guidance. They’re grocery staples, inspiring new recipes. Benefits grow with consistent use, fitting weekly meal plans. They support weight management, enhance skin health, and may reduce cancer risk. Cultural recipes globalize their appeal, and growing some like chia is feasible. Their sustainability and economic value make them a smart choice. Online forums share creative uses, while studies confirm their longevity link. They balance diets across lifestyles, from vegan to omnivorous, with a rich sensory experience.', CAST(N'2025-06-09T16:34:00.000' AS DateTime))
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (3, 9, N'How to Make a Healthy Smoothie at Home', N'smoothie.jpg', N'Making a healthy smoothie at home is a simple, customizable way to boost nutrition. Begin with a base like almond milk or water for a light texture, adding spinach or kale for green nutrients. A banana provides natural sweetness and creaminess, while berries like strawberries or blueberries bring antioxidants. A scoop of protein powder supports muscle repair, and chia or flaxseeds add omega-3s. Frozen fruits replace ice, enhancing flavor, and a tablespoon of peanut butter contributes healthy fats. Blend until smooth, serve immediately, and adjust ingredients to taste. Smoothies suit breakfast or snacks, replacing meals if balanced. Experiment with fruits, add yogurt for probiotics, and avoid sugary juices. A powerful blender ensures quality, cleaned post-use. Store leftovers for 24 hours, shaking before drinking. They aid weight loss, fit busy mornings, and appeal to kids with cocoa. Track ingredients for balance, making it a daily ritual. They boost metabolism, hydrate, and support detox. Seasonal fruits optimize nutrients, while prepping in batches saves time. Online tutorials offer inspiration, and pairing with nuts enhances satiety. They reduce cravings, align with dietary goals, and promote gut health. Cultural twists add global flair, and their convenience suits all ages.', CAST(N'2025-06-09T16:35:00.000' AS DateTime))
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (4, 9, N'The Power of Whole Grains', N'whole_grains.jpg', N'Whole grains like brown rice, oats, and quinoa are nutritional treasures, retaining bran and germ for maximum benefit. They offer complex carbohydrates for sustained energy, fiber for digestion, and antioxidants to fight inflammation. Brown rice swaps white for better nutrition, oats fuel heart-healthy breakfasts, and quinoa provides complete protein. They regulate blood sugar, lower diabetes risk, and reduce cholesterol. Enjoy them in soups, salads, or sides, cooking in bulk to save time. Pair with veggies for balance, try whole-grain pasta, and avoid overprocessing. Check labels for 100% whole grain, starting with small portions. They support gut health with prebiotics, are affordable, and widely available. Lightly toasting enhances flavor, while seasonal varieties offer freshness. They promote fullness for weight control, boost energy, and may prevent heart disease. Growing your own grains is an option, and cultural recipes enrich meals. Online communities share ideas, and studies link them to longevity. They fit all diets, improve skin, and support muscle recovery. Their sustainability and versatility make them a dietary staple.', CAST(N'2025-06-09T16:36:00.000' AS DateTime))
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (5, 9, N'Healthy Snack Ideas for Busy Days', N'healthy_snacks.jpg', N'Healthy snacks like apple slices with almond butter keep you energized on busy days. Carrot sticks with hummus offer fiber and protein, while almonds provide healthy fats. Greek yogurt with honey adds probiotics, and rice cakes with avocado are light. Trail mix with nuts and fruit is portable, and cut veggies stay ready in the fridge. Hard-boiled eggs pack protein, and cheese with whole-grain crackers satisfies. Avoid sugary options, prepping snacks ahead in small containers. Snack every 3-4 hours to prevent overeating, choosing mixed macros. Hydrate with water, and kids enjoy these too. Keep snacks visible, rotating flavors to avoid boredom. They support health goals, boost metabolism, and fit tight schedules. Batch prepping saves time, while seasonal ingredients optimize nutrients. Online recipes inspire variety, and pairing with tea enhances enjoyment. They reduce cravings, support weight management, and promote gut health. Cultural snacks add diversity, and their convenience suits all ages.', CAST(N'2025-06-09T16:37:00.000' AS DateTime))
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (6, 9, N'Why You Should Include Nuts in Your Diet', N'nuts.jpg', N'Nuts like almonds, walnuts, and cashews are nutrient-dense, offering monounsaturated fats for heart health. Almonds provide vitamin E for skin, walnuts deliver omega-3s for brain function, and cashews offer magnesium for muscles. They lower bad cholesterol, promote heart health, and serve as a small handful snack. Add to salads for crunch, use in baking, and avoid salted versions. Store in airtight containers, buy in bulk, and improve blood sugar control. They aid weight management moderately, pair with fruit, and suit gradual introduction if allergic. Consult a doctor if unsure, and use in cooking for versatility. They boost immunity, enhance skin, and may reduce cancer risk. Growing some nuts is possible, and cultural recipes globalize their use. Online ideas abound, while studies link them to longevity. They fit all diets, support recovery, and offer a rich sensory experience.', CAST(N'2025-06-09T16:38:00.000' AS DateTime))
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (7, 9, N'Cooking with Olive Oil: A Healthy Choice', N'olive_oil.jpg', N'Olive oil, especially extra virgin, is a Mediterranean diet staple, rich in monounsaturated fats for heart health. It retains antioxidants, suits salad dressings, and enhances sautéed veggies. Avoid high-heat frying, store in dark bottles, and use a tablespoon daily for cardiovascular benefits. It reduces inflammation, improves digestion, and pairs with herbs. Use as a bread dip, buy from reputable sources, and check acidity levels. Incorporate into marinades, substitute for butter, and start small. It boosts nutrient absorption, is a kitchen staple, and supports weight control. Seasonal uses inspire recipes, while cultural dishes highlight its value. Online forums share tips, and studies back its anti-inflammatory role. It’s sustainable, affordable, and fits all diets, enhancing meal enjoyment.', CAST(N'2025-06-09T16:39:00.000' AS DateTime))
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (8, 9, N'The Role of Fruits in Weight Management', N'fruits.jpg', N'Fruits like apples and berries aid weight management with low calories and high fiber. Apples’ pectin promotes fullness, while berries’ antioxidants boost metabolism. Eat whole to avoid sugars, pair with protein, and include in bowls or salads. Avoid juices, use seasonal options, and store properly. Freeze berries for smoothies, and aim for 2-3 servings. They regulate blood sugar, reduce cravings, and add variety. Kids enjoy them as treats, and tracking intake aids goals. Consult a nutritionist, and they support gut health. Cultural recipes enrich meals, while growing your own ensures freshness. Online ideas inspire, and studies link them to longevity. They’re sustainable, versatile, and a natural weight loss ally.', CAST(N'2025-06-09T16:40:00.000' AS DateTime))
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (9, 9, N'Healthy Breakfast Recipes to Start Your Day', N'breakfast.jpg', N'A healthy breakfast with oatmeal or eggs sets a positive tone. Oatmeal with berries offers fiber, scrambled eggs with spinach pack protein, and toast with avocado satisfies. Greek yogurt with nuts adds probiotics, and smoothies with kale boost nutrients. Avoid sugary cereals, prep overnight oats, and use fresh ingredients. Cook in batches, add seeds, and pair with tea. Kids love fruit pancakes, and tracking calories helps. Experiment with spices, boost metabolism, and make it daily. Consult a dietitian, and seasonal options optimize taste. Online recipes inspire, and they support weight goals. Cultural twists add flair, fitting all ages.', CAST(N'2025-06-09T16:41:00.000' AS DateTime))
INSERT [dbo].[Blog] ([id], [user_id], [title], [image], [description], [created_at]) VALUES (10, 9, N'Detox with Healthy Soups', N'detox_soup.jpg', N'Detox soups with veggie broth cleanse naturally. Add kale and carrots for nutrients, ginger for inflammation, and turmeric for antioxidants. Avoid cream, blend if preferred, and season with parsley. Cook in bulk, store airtight, and reheat gently. Pair with bread, aid digestion, and flush toxins. Kids enjoy mild versions, adjust spices, and consult a doctor for long-term use. They hydrate, boost immunity, and fit busy schedules. Seasonal ingredients enhance flavor, while online recipes inspire. Studies back detox benefits, and they’re sustainable. Cultural uses add tradition, making them a health boost.', CAST(N'2025-06-09T16:42:00.000' AS DateTime))`;
        document.querySelector(".input-area button").addEventListener("click", sendMessage);
        const conversations = [
//     {
//        "role": "user",
//        "parts": [
//          {
//            "text": "Hello"
//          }
//        ]
//      },
//      {
//        "role": "model",
//        "parts": [
//          {
//            "text": "Great to meet you. What would you like to know?"
//          }
//        ]
//      },
//      {
//        "role": "user",
//        "parts": [
//          {
//            "text": "I have two dogs in my house. How many paws are in my house?"
//          }
//        ]
//      }
        ];
        function sendMessage() {

        //lay du lieu trong input
        const userMessage = document.querySelector(".input-area input").value;
                // neu co nhap du lieu thi moi dua data len .chat
                if (userMessage.length) {
        // xoa du lieu trong input
        document.querySelector(".input-area input").value = "";
                document.querySelector(".chatwindow .chat").insertAdjacentHTML("beforeend", `<div class="user">
                    <p>${userMessage}</p>
                </div >`);
                //them vao conversation
                conversations.push({
                "role": "user",
                        "parts": [
                        {
                        "text": userMessage,
                        }
                        ]
                });
                // gui du lieu cho model va tra ve ket qua
                sendData();
        }

        async function sendData() {
        document.querySelector(".chatwindow .chat").insertAdjacentHTML("beforeend", `
        <div class="model typing">
            <p>Please wait for the response...</p>
        </div>`);
                const response = await fetch("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + Gemini_Key, {
                method: "POST",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify({
                        "system_instruction": {
                        "parts": [
                        {
                        "text": trainingInfor + "\n" + trainingDatabase,
                        }
                        ]
                        }, "contents": conversations,
                        }),
                        // …
                });
                const data = await response.json();
                console.log(data);
                const typingIndicator = document.querySelector(".chatwindow .chat .typing");
                if (typingIndicator)
                typingIndicator.remove();
                //in du lieu vao .chat
                document.querySelector(".chatwindow .chat").insertAdjacentHTML("beforeend", `<div class="model">
                    <p>${data.candidates[0].content.parts[0].text}</p>
                </div >`);
                //them du lieu tra ve cua model vao conversations
                conversations.push({
                "role": "model",
                        "parts": [
                        {
                        "text": data.candidates[0].content.parts[0].text,
                        }
                        ]
                })
        }
        }

        document.querySelector(".toggle-chatbot").addEventListener("click", function () {
            document.querySelector(".chatwindow").classList.toggle("hidden");
            if (!document.querySelector(".chatwindow").classList.contains("hidden")) {
        document.querySelector(".toggle-chatbot").style.display = "none";
            }
        });
        
        
        document.querySelector(".chatwindow .close").addEventListener("click", function () {
            document.querySelector(".chatwindow").classList.add("hidden");
            document.querySelector(".toggle-chatbot").style.display = "block";
        });
        
        
        document.querySelector(".input-area input").addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                sendMessage();
            }
        });