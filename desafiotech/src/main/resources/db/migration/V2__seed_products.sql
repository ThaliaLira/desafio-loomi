INSERT INTO products (
    id,
    product_id,
    name,
    product_type,
    price,
    stock_quantity,
    active,
    metadata,
    created_at,
    updated_at
) VALUES
      (gen_random_uuid(), 'BOOK-CC-001', 'Clean Code', 'PHYSICAL', 89.90, 150, true, NULL, NOW(), NOW()),
      (gen_random_uuid(), 'LAPTOP-PRO-2024', 'Laptop Pro', 'PHYSICAL', 5499.00, 8, true, NULL, NOW(), NOW()),
      (gen_random_uuid(), 'LAPTOP-MBP-M3-001', 'MacBook Pro M3', 'PHYSICAL', 12999.00, 25, true, NULL, NOW(), NOW()),

      (gen_random_uuid(), 'SUB-PREMIUM-001', 'Premium Monthly', 'SUBSCRIPTION', 49.90, NULL, true, NULL, NOW(), NOW()),
      (gen_random_uuid(), 'SUB-BASIC-001', 'Basic Monthly', 'SUBSCRIPTION', 19.90, NULL, true, NULL, NOW(), NOW()),
      (gen_random_uuid(), 'SUB-ENTERPRISE-001', 'Enterprise Plan', 'SUBSCRIPTION', 299.00, NULL, true, NULL, NOW(), NOW()),
      (gen_random_uuid(), 'SUB-ADOBE-CC-001', 'Adobe Creative Cloud', 'SUBSCRIPTION', 159.00, NULL, true, NULL, NOW(), NOW()),

      (gen_random_uuid(), 'EBOOK-JAVA-001', 'Effective Java', 'DIGITAL', 39.90, NULL, true, '{"licenses":1000}', NOW(), NOW()),
      (gen_random_uuid(), 'EBOOK-DDD-001', 'Domain-Driven Design', 'DIGITAL', 59.90, NULL, true, '{"licenses":500}', NOW(), NOW()),
      (gen_random_uuid(), 'EBOOK-SWIFT-001', 'Swift Programming', 'DIGITAL', 49.90, NULL, true, '{"licenses":800}', NOW(), NOW()),
      (gen_random_uuid(), 'COURSE-KAFKA-001', 'Kafka Mastery', 'DIGITAL', 299.00, NULL, true, '{"licenses":500}', NOW(), NOW()),

      (gen_random_uuid(), 'GAME-2025-001', 'Epic Game 2025', 'PRE_ORDER', 249.90, NULL, true, '{"releaseDate":"2025-06-01","preOrderSlots":1000}', NOW(), NOW()),
      (gen_random_uuid(), 'PRE-PS6-001', 'PlayStation 6', 'PRE_ORDER', 4999.00, NULL, true, '{"releaseDate":"2025-11-15","preOrderSlots":500}', NOW(), NOW()),
      (gen_random_uuid(), 'PRE-IPHONE16-001', 'iPhone 16 Pro', 'PRE_ORDER', 7999.00, NULL, true, '{"releaseDate":"2025-09-20","preOrderSlots":2000}', NOW(), NOW()),

      (gen_random_uuid(), 'CORP-LICENSE-ENT', 'Enterprise License', 'CORPORATE', 15000.00, NULL, true, NULL, NOW(), NOW()),
      (gen_random_uuid(), 'CORP-CHAIR-ERG-001', 'Ergonomic Chair Bulk', 'CORPORATE', 899.00, 500, true, NULL, NOW(), NOW());