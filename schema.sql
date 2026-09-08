CREATE TABLE IF NOT EXISTS blood_centers (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    address TEXT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS blood_stock (
    id UUID PRIMARY KEY,
    center_id UUID NOT NULL REFERENCES blood_centers(id),
    blood_group TEXT NOT NULL,
    component TEXT NOT NULL,
    units_available INTEGER NOT NULL DEFAULT 0,
    last_verified_at TIMESTAMPTZ,
    source TEXT
);

CREATE TABLE IF NOT EXISTS emergency_requests (
    id UUID PRIMARY KEY,
    requester_id UUID,
    blood_group TEXT NOT NULL,
    component TEXT,
    units_needed INTEGER NOT NULL,
    status TEXT NOT NULL DEFAULT 'OPEN',
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
