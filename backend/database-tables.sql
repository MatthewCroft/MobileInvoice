CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       created_at TIMESTAMP DEFAULT NOW(),
                       updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE customers (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           user_id UUID REFERENCES users(id) ON DELETE CASCADE,
                           name VARCHAR(255) NOT NULL,
                           phone VARCHAR(50),
                           email VARCHAR(255),
                           address TEXT,
                           created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE invoices (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          user_id UUID REFERENCES users(id) ON DELETE CASCADE,
                          customer_id UUID REFERENCES customers(id) ON DELETE SET NULL,
                          invoice_number VARCHAR(50) UNIQUE,
                          issue_date DATE NOT NULL DEFAULT CURRENT_DATE,
                          due_date DATE,
                          status VARCHAR(20) NOT NULL DEFAULT 'unpaid', -- 'unpaid', 'paid', 'overdue'
                          tax_amount NUMERIC(10,2),
                          subtotal NUMERIC(10,2),
                          total NUMERIC(10,2),
                          notes TEXT,
                          created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE invoice_items (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               invoice_id UUID REFERENCES invoices(id) ON DELETE CASCADE,
                               description TEXT NOT NULL,
                               quantity INTEGER NOT NULL DEFAULT 1,
                               unit_price NUMERIC(10,2) NOT NULL,
                               total_price NUMERIC(10,2) GENERATED ALWAYS AS (quantity * unit_price) STORED
);

CREATE TABLE payments (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          invoice_id UUID REFERENCES invoices(id) ON DELETE CASCADE,
                          amount NUMERIC(10,2) NOT NULL,
                          method VARCHAR(50), -- 'card', 'cash', 'check', 'ach'
                          status VARCHAR(20) NOT NULL DEFAULT 'completed', -- future: 'pending', 'failed'
                          payment_date TIMESTAMP DEFAULT NOW(),
                          stripe_payment_id VARCHAR(255) -- optional, if using Stripe
);



