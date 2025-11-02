from app import db
from flask_login import UserMixin
from sqlalchemy.orm import Mapped, mapped_column, relationship
from sqlalchemy import Integer, String, Boolean, Float, ForeignKey, Enum, DateTime
from datetime import datetime
import enum, uuid


class ReceiptTypeEnum(enum.Enum):
    PAID = "paid"
    UNPAID = "un-paid"
    GCASH = "gcash"


class User(UserMixin, db.Model):
    __tablename__ = "users"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    email: Mapped[str] = mapped_column(String(100), unique=True)
    password: Mapped[str] = mapped_column(String(200), nullable=False)
    activated: Mapped[bool] = mapped_column(Boolean, default=False)
    otp: Mapped[str] = mapped_column(String(4), nullable=True)

    # Relationships
    items = relationship("Item", back_populates="user", cascade="all, delete-orphan")
    receipts = relationship("Receipt", back_populates="user", cascade="all, delete-orphan")


class Item(db.Model):
    __tablename__ = "items"

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    user_id: Mapped[int] = mapped_column(ForeignKey("users.id"), nullable=False)
    name: Mapped[str] = mapped_column(String(100), unique=True)
    price: Mapped[float] = mapped_column(Float, nullable=False)

    # Relationship back to user
    user = relationship("User", back_populates="items")


class Receipt(db.Model):
    __tablename__ = "receipts"

    id: Mapped[str] = mapped_column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    user_id: Mapped[int] = mapped_column(ForeignKey("users.id"), nullable=False)
    type: Mapped[ReceiptTypeEnum] = mapped_column(Enum(ReceiptTypeEnum), nullable=False)
    name: Mapped[str] = mapped_column(String(100), nullable=True)
    created_date: Mapped[datetime] = mapped_column(DateTime, default=datetime.now)
    updated_date: Mapped[datetime] = mapped_column(DateTime, default=datetime.now, nullable=True)
    items: Mapped[str] = mapped_column(String)  # JSON or serialized list of items
    total: Mapped[float] = mapped_column(Float, default=0, nullable=True)
    change: Mapped[float] = mapped_column(Float, default=0, nullable=True)
    cash: Mapped[float] = mapped_column(Float, default=0, nullable=True)

    # Relationship back to user
    user = relationship("User", back_populates="receipts")
