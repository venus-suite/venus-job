import homeBg from '@/assets/bg.png';

const Home = () => (
  <div
    style={{
      height: 'calc(100vh - 48px)',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      background: '#fff',
    }}
  >
    <div
      style={{
        transform: 'translateY(-10%)',
      }}
    >
      <img src={homeBg} width="90%" alt="" />
    </div>
  </div>
);

export default Home;
