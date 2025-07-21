import 'package:flutter/material.dart';
import 'recommendation_result_page.dart';

class RecommendationSurveyPage extends StatefulWidget {
  const RecommendationSurveyPage({Key? key}) : super(key: key);

  @override
  State<RecommendationSurveyPage> createState() => _RecommendationSurveyPageState();
}

class _RecommendationSurveyPageState extends State<RecommendationSurveyPage> {
  // 선택된 답변들을 저장할 리스트 (인덱스는 질문 번호, 값은 선택된 답변 인덱스)
  List<int?> selectedAnswers = List.filled(10, null);

  // 더미 질문 데이터 (나중에 실제 질문으로 교체)
  final List<Map<String, dynamic>> questions = [
    {
      'question': '하루 중 강아지와 함께 보낼 수 있는 시간은 어느 정도인가요?',
      'options': [
        '재택근무 등으로 대부분의 시간을 함께할 수 있다.',
        '출퇴근 시간 외 저녁과 주말은 온전히 함께 보낼 수 있다.',
        '혼자 있는 시간이 길어, 독립적인 성향의 강아지를 선호한다.'
      ]
    },
    {
      'question': '어떤 성격의 강아지와 함께하고 싶으신가요?',
      'options': [
        '에너지가 넘치고 함께 뛰어놀기 좋아하는 활발한 친구',
        '무릎에 앉아 애교 부리는 것을 좋아하는 사랑스러운 껌딱지',
        '듬직하고 조용하며, 가끔 혼자만의 시간을 즐기는 독립적인 동반자'
      ]
    },
    {
      'question': '강아지를 길러본 경험이 있으신가요?',
      'options': [
        '여러 마리의 강아지를 키워본 경험이 있다.',
        '한 마리 정도 키워본 경험이 있다.',
        '처음 키우는 강아지다.'
      ]
    },
    {
      'question': '어떤 크기의 강아지를 선호하시나요?',
      'options': [
        '작은 크기 (5kg 이하)',
        '중간 크기 (5-20kg)',
        '큰 크기 (20kg 이상)'
      ]
    },
    {
      'question': '어떤 활동을 주로 하고 싶으신가요?',
      'options': [
        '공놀이, 산책 등 적극적인 활동',
        '포옹, 스킨십 등 애정 표현',
        '조용히 함께 있는 시간'
      ]
    },
    {
      'question': '거주 환경은 어떻게 되시나요?',
      'options': [
        '넓은 마당이 있는 단독주택',
        '아파트나 빌라',
        '원룸이나 작은 공간'
      ]
    },
    {
      'question': '가족 구성원은 어떻게 되시나요?',
      'options': [
        '성인만 있는 가족',
        '아이가 있는 가족',
        '노인과 함께 사는 가족'
      ]
    },
    {
      'question': '어떤 털 길이를 선호하시나요?',
      'options': [
        '짧은 털',
        '중간 길이 털',
        '긴 털'
      ]
    },
    {
      'question': '어떤 목적으로 강아지를 키우고 싶으신가요?',
      'options': [
        '운동 동반자',
        '정서적 교감',
        '가족의 일원'
      ]
    },
    {
      'question': '강아지 훈련에 투자할 수 있는 시간은?',
      'options': [
        '충분한 시간을 투자할 수 있다.',
        '적당한 시간을 투자할 수 있다.',
        '최소한의 훈련만 가능하다.'
      ]
    },
  ];

  void _selectAnswer(int questionIndex, int answerIndex) {
    setState(() {
      selectedAnswers[questionIndex] = answerIndex;
    });
  }

  void _showResult() {
    // 모든 질문에 답변했는지 확인
    if (selectedAnswers.contains(null)) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('모든 질문에 답변해주세요.'),
          backgroundColor: Colors.red,
        ),
      );
      return;
    }

    // 결과 페이지로 이동
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => RecommendationResultPage(answers: selectedAnswers),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xFFF6F6F6),
      appBar: AppBar(
        title: const Text(
          '맞춤동물 추천',
          style: TextStyle(
            fontWeight: FontWeight.bold,
            color: Colors.black,
          ),
        ),
        backgroundColor: Colors.white,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: Colors.black),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16),
          child: Column(
            children: [
              // 질문들
              ...List.generate(questions.length, (index) {
                return _buildQuestionCard(index);
              }),
              
              const SizedBox(height: 32),
              
              // 결과 버튼
              SizedBox(
                width: double.infinity,
                height: 56,
                child: ElevatedButton(
                  onPressed: _showResult,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Color(0xFF4CAF50),
                    foregroundColor: Colors.white,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(16),
                    ),
                    elevation: 0,
                  ),
                  child: const Text(
                    '나에게 맞는 강아지는?',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
              
              const SizedBox(height: 32),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildQuestionCard(int questionIndex) {
    final question = questions[questionIndex];
    final isSelected = selectedAnswers[questionIndex] != null;
    
    return Container(
      margin: const EdgeInsets.only(bottom: 24),
      child: Stack(
        children: [
          // 연결선 (마지막 질문 제외)
          if (questionIndex < questions.length - 1)
            Positioned(
              left: 30,
              top: 80,
              bottom: -24,
              child: Container(
                width: 2,
                color: Colors.grey[300],
              ),
            ),
          
          // 질문 카드
          Container(
            margin: const EdgeInsets.only(top: 20),
            padding: const EdgeInsets.all(20),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(16),
              boxShadow: [
                BoxShadow(
                  color: Colors.black.withOpacity(0.05),
                  offset: const Offset(0, 2),
                  blurRadius: 8,
                ),
              ],
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  question['question'],
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.w600,
                    color: Colors.black87,
                  ),
                ),
                const SizedBox(height: 16),
                
                // 선택지들
                ...List.generate(question['options'].length, (optionIndex) {
                  final isOptionSelected = selectedAnswers[questionIndex] == optionIndex;
                  
                  return GestureDetector(
                    onTap: () => _selectAnswer(questionIndex, optionIndex),
                    child: Container(
                      margin: const EdgeInsets.only(bottom: 12),
                      padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 20),
                      decoration: BoxDecoration(
                        color: isOptionSelected ? Color(0xFFE8F5E8) : Colors.white,
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(
                          color: isOptionSelected ? Color(0xFF4CAF50) : Colors.grey[300]!,
                          width: isOptionSelected ? 2 : 1,
                        ),
                      ),
                      child: Center(
                        child: Text(
                          question['options'][optionIndex],
                          style: TextStyle(
                            fontSize: 14,
                            color: isOptionSelected ? Color(0xFF4CAF50) : Colors.black87,
                            fontWeight: isOptionSelected ? FontWeight.w600 : FontWeight.normal,
                          ),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ),
                  );
                }),
              ],
            ),
          ),
          
          // Q 라벨 (고급스러운 디자인)
          Positioned(
            left: 16,
            top: -1, // 더 위쪽으로 조정
            child: Container(
              width: 44,
              height: 44,
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                  colors: [
                    Color(0xFF667eea),
                    Color(0xFF764ba2),
                  ],
                ),
                borderRadius: BorderRadius.circular(22),
                boxShadow: [
                  BoxShadow(
                    color: Color(0xFF667eea).withOpacity(0.3),
                    offset: const Offset(0, 4),
                    blurRadius: 12,
                  ),
                ],
              ),
              child: Center(
                child: Text(
                  'Q${questionIndex + 1}',
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    color: Colors.white,
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
} 