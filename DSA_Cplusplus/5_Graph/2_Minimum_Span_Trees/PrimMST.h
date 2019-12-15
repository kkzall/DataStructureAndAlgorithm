#pragma once

#include <iostream>
#include <vector>
#include <cassert>
#include "Edge.h"
#include "IndexMinHeap.h"

using namespace std;

// ʹ���Ż���Prim�㷨��ͼ����С������
template<typename Graph, typename Weight>
class PrimMST {
	
private:
	Graph& G;					// ͼ������
	IndexMinHeap<Weight> ipq;		// ��С������, �㷨�������ݽṹ
	vector<Edge<Weight>*> edgeTo;	// ���ʵĵ�����Ӧ�ı�, �㷨�������ݽṹ
	bool* marked;                // �������, ���㷨���й����б�ǽڵ�i�Ƿ񱻷���
	vector<Edge<Weight>> mst;     // ��С�����������������б�
	Weight mstWeight;            // ��С��������Ȩֵ

	// ���ʽڵ�v
	void visit(int v) {
		assert(!marked[v]);
		marked[v] = true;

		// ���ͽڵ�v�����ӵ�δ���ʵ���һ�˵�, ����֮�����ӵı�, ������С����
		typename Graph::adjIterator adj(G, v);
		for (Edge<Weight>* e = adj.begin(); !adj.end(); e = adj.next()) {
			int w = e->other(v);

			// ����ߵ���һ�˵�δ������
			if (!marked[w])
			{
				// �����û�п��ǹ�����˵�, ֱ�ӽ�����˵����֮�����ӵı߼���������
				if (!edgeTo[w])
				{
					edgeTo[w] = e;
					ipq.insert(w, e->wt());
				}
				// ���������������˵�, �����ڵı߱�֮ǰ���ǵı߸���, ������滻
				else if (e->wt() < edgeTo[w]->wt()) {
					edgeTo[w] = e;
					ipq.change(w, e->wt());
				}
			}
		}
	}

public:
	// ���캯��, ʹ��Prim�㷨��ͼ����С������
	PrimMST(Graph& graph) :G(graph), ipq(IndexMinHeap<double>(graph.V())) {

		assert(graph.E() >= 1);

		// �㷨��ʼ��
		marked = new bool[G.V()];
		for (int i = 0; i < G.V(); i++) {
			marked[i] = false;
			edgeTo.push_back(NULL);
		}
		mst.clear();

		// Prim
		visit(0);
		while (!ipq.isEmpty())
		{
			// ʹ����С�������ҳ��Ѿ����ʵı���Ȩֵ��С�ı�
			// ��С�������д洢���ǵ������, ͨ����������ҵ����Ӧ�ı�
			int v = ipq.extractMinIndex();
			assert(edgeTo[v]);
			mst.push_back(*edgeTo[v]);
			visit(v);
		}

		mstWeight = mst[0].wt();
		for (int i = 1; i < mst.size(); i++)
			mstWeight += mst[i].wt();
	}

	~PrimMST() {
		delete[] marked;
	}

	vector<Edge<Weight>> mstEdges() {
		return mst;
	}

	Weight result() {
		return mstWeight;
	}
};