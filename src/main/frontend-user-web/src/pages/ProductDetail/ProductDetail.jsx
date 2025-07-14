import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useCart } from '../../contexts/CartContext';
import './ProductDetail.css';

const products = [
  {
    id: 1,
    name: '강아지 사료 2kg',
    price: 18000,
    image: '/images/dogfood.jpg',
    description: '우리 강아지를 위한 영양 가득한 사료입니다. 2kg 용량으로 오래 급여할 수 있습니다.',
    details: '신선한 재료와 균형 잡힌 영양 설계로 반려견의 건강을 지켜주는 프리미엄 사료입니다. 전연령견에게 적합합니다.'
  },
  {
    id: 2,
    name: '고양이 장난감',
    price: 7500,
    image: '/images/dogfood.jpg',
    description: '고양이의 사냥 본능을 자극하는 재미있는 장난감입니다. 안전한 소재로 만들어졌습니다.',
    details: '흔들면 소리가 나는 방울이 들어있어 고양이의 호기심을 자극하고, 스트레스 해소에 도움을 줍니다.'
  },
  {
    id: 3,
    name: '반려동물 방석',
    price: 22000,
    image: '/images/dogfood.jpg',
    description: '푹신하고 편안한 반려동물 방석입니다. 세탁이 용이하여 위생적으로 관리할 수 있습니다.',
    details: '분리형 커버로 세탁이 간편하며, 바닥에는 미끄럼 방지 처리가 되어 있어 안전합니다.'
  },
];

const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const { addToCart } = useCart();
  const [quantity, setQuantity] = useState(1);
  const [activeTab, setActiveTab] = useState('details');
  const navigate = useNavigate();

  useEffect(() => {
    const foundProduct = products.find(p => p.id === parseInt(id));
    setProduct(foundProduct);
  }, [id]);

  if (!product) {
    return <div className="product-detail-page">상품을 찾을 수 없습니다.</div>;
  }

  const handleQuantityChange = (amount) => {
    setQuantity(prev => Math.max(1, prev + amount));
  };

  const handleAddToCart = () => {
    addToCart({ ...product, quantity });
    alert(`${product.name} ${quantity}개가 장바구니에 추가되었습니다.`);
  };

  const handleBuyNow = () => {
    navigate('/checkout', { state: { buyNowProduct: product, buyNowQuantity: quantity } });
  };

  const totalPrice = product.price * quantity;

  return (
    <div className="product-detail-page">
      <div className="product-detail-container">
        <div className="product-image-section">
          <img src={product.image} alt={product.name} className="product-detail-img" />
        </div>
        <div className="product-info-section">
          <h2 className="product-detail-name">{product.name}</h2>
          <p className="product-detail-description">{product.description}</p>
          <div className="quantity-selector">
            <button onClick={() => handleQuantityChange(-1)}>-</button>
            <span>{quantity}</span>
            <button onClick={() => handleQuantityChange(1)}>+</button>
          </div>
          <div className="total-price-section">
            <span>총 상품금액</span>
            <span className="total-price-amount">{totalPrice.toLocaleString()}원</span>
          </div>
          <div className="product-actions">
            <button className="add-to-cart-btn" onClick={handleAddToCart}>장바구니 담기</button>
            <button className="buy-now-btn" onClick={handleBuyNow}>바로 구매</button>
          </div>
        </div>
      </div>

      <div className="product-info-tabs">
        <div className="tab-header">
          <button className={activeTab === 'details' ? 'active' : ''} onClick={() => setActiveTab('details')}>상품상세정보</button>
          <button className={activeTab === 'shipping' ? 'active' : ''} onClick={() => setActiveTab('shipping')}>배송안내</button>
          <button className={activeTab === 'exchange' ? 'active' : ''} onClick={() => setActiveTab('exchange')}>교환 및 반품안내</button>
          <button className={activeTab === 'reviews' ? 'active' : ''} onClick={() => setActiveTab('reviews')}>상품후기</button>
          <button className={activeTab === 'qna' ? 'active' : ''} onClick={() => setActiveTab('qna')}>상품문의</button>
        </div>
        <div className="tab-content">
          {activeTab === 'details' && <div className="tab-pane">{product.details}</div>}
          {activeTab === 'shipping' && <div className="tab-pane">
            <h4>배송안내</h4>
            <p>- 배송비: 3,000원 (50,000원 이상 구매 시 무료배송)</p>
            <p>- 배송기간: 결제 완료 후 2~3일 소요 (주말/공휴일 제외)</p>
            <p>- 도서/산간 지역은 추가 배송비가 발생할 수 있습니다.</p>
          </div>}
          {activeTab === 'exchange' && <div className="tab-pane">
            <h4>교환 및 반품 안내</h4>
            <p>- 상품 수령 후 7일 이내에 신청 가능합니다.</p>
            <p>- 단순 변심에 의한 교환/반품 시 왕복 배송비는 고객님 부담입니다.</p>
            <p>- 상품 불량 또는 오배송의 경우 배송비는 판매자가 부담합니다.</p>
          </div>}
          {activeTab === 'reviews' && <div className="tab-pane">상품후기가 아직 없습니다.</div>}
          {activeTab === 'qna' && <div className="tab-pane">상품문의가 아직 없습니다.</div>}
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;

