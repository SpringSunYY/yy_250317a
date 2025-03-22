<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平台" prop="platform">
        <el-input
          v-model="queryParams.platform"
          placeholder="请输入平台"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺" prop="storeId">
        <el-select
          v-model="queryParams.storeId"
          filterable
          remote
          reserve-keyword
          placeholder="请输入店铺名称"
          :remote-method="selectStoreInfoList"
          :loading="storeInfoLoading"
        >
          <el-option
            v-for="item in storeInfoList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="站点" prop="marketplaceId">
        <el-input
          v-model="queryParams.marketplaceId"
          placeholder="请输入站点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单号" prop="amazonOrderId">
        <el-input
          v-model="queryParams.amazonOrderId"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订购时间" prop="purchaseDate">
        <el-input
          v-model="queryParams.purchaseDate"
          placeholder="请输入订购时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="ASIN" prop="asin">
        <el-input
          v-model="queryParams.asin"
          placeholder="请输入ASIN"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="品名" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入品名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="扫码时间">
        <el-date-picker
          v-model="daterangeScanTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="买家姓名" prop="buyerName">
        <el-input
          v-model="queryParams.buyerName"
          placeholder="请输入买家姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="买家邮箱" prop="buyerEmail">
        <el-input
          v-model="queryParams.buyerEmail"
          placeholder="请输入买家邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请求评论状态" prop="begEvaluateStatus">
        <el-select v-model="queryParams.begEvaluateStatus" placeholder="请选择请求评论状态" clearable>
          <el-option
            v-for="dict in dict.type.beg_evaluate_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="评论负责人" prop="evaluatePrincipal">
        <el-input
          v-model="queryParams.evaluatePrincipal"
          placeholder="请输入评论负责人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="售后标记" prop="afterSaleSign">
        <el-select v-model="queryParams.afterSaleSign" placeholder="请选择售后标记" clearable>
          <el-option
            v-for="dict in dict.type.after_sale_sign"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['manage:orderInfo:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['manage:orderInfo:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['manage:orderInfo:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['manage:orderInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="编号" align="center" v-if="columns[0].visible" prop="id"/>
      <el-table-column label="平台" :show-overflow-tooltip="true" align="center" v-if="columns[1].visible"
                       prop="platform"
      />
      <el-table-column label="店铺" :show-overflow-tooltip="true" align="center" v-if="columns[2].visible"
                       prop="storeName"
      />
      <el-table-column label="站点" :show-overflow-tooltip="true" align="center" v-if="columns[3].visible"
                       prop="marketplaceId"
      />
      <el-table-column label="订单号" :show-overflow-tooltip="true" align="center" v-if="columns[4].visible"
                       prop="amazonOrderId"
      />
      <el-table-column label="订购时间" :show-overflow-tooltip="true" align="center" v-if="columns[5].visible"
                       prop="purchaseDate"
      />
      <el-table-column label="ASIN" :show-overflow-tooltip="true" align="center" v-if="columns[6].visible" prop="asin"/>
      <el-table-column label="品名" :show-overflow-tooltip="true" align="center" v-if="columns[7].visible"
                       prop="title"
      />
      <el-table-column label="亚马逊评价链接" :show-overflow-tooltip="true" align="center" v-if="columns[8].visible"
                       prop="comment"
      />
      <el-table-column label="亚马逊评价时间" align="center" v-if="columns[9].visible" prop="evaluateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.evaluateTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="亚马逊评分星级" :show-overflow-tooltip="true" align="center" v-if="columns[10].visible"
                       prop="evaluateLevel"
      />
      <el-table-column label="亚马逊评价内容" :show-overflow-tooltip="true" align="center" v-if="columns[11].visible"
                       prop="evaluateContent"
      />
      <el-table-column label="亚马逊商品链接" :show-overflow-tooltip="true" align="center" v-if="columns[12].visible"
                       prop="goodsLink"
      />
      <el-table-column label="扫码时间" align="center" v-if="columns[13].visible" prop="scanTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.scanTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="买家姓名" :show-overflow-tooltip="true" align="center" v-if="columns[14].visible"
                       prop="buyerName"
      />
      <el-table-column label="买家邮箱" :show-overflow-tooltip="true" align="center" v-if="columns[15].visible"
                       prop="buyerEmail"
      />
      <el-table-column label="买家评分" :show-overflow-tooltip="true" align="center" v-if="columns[16].visible"
                       prop="buyerLevel"
      />
      <el-table-column label="买家评论" :show-overflow-tooltip="true" align="center" v-if="columns[17].visible"
                       prop="buyerEvaluate"
      />
      <el-table-column label="请求评论状态" align="center" v-if="columns[18].visible" prop="begEvaluateStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.beg_evaluate_status" :value="scope.row.begEvaluateStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="请求评论内容" :show-overflow-tooltip="true" align="center" v-if="columns[19].visible"
                       prop="begEvalueteContent"
      />
      <el-table-column label="评论负责人" :show-overflow-tooltip="true" align="center" v-if="columns[20].visible"
                       prop="evaluatePrincipal"
      />
      <el-table-column label="售后标记" align="center" v-if="columns[21].visible" prop="afterSaleSign">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.after_sale_sign" :value="scope.row.afterSaleSign"/>
        </template>
      </el-table-column>
      <el-table-column label="创建人" :show-overflow-tooltip="true" align="center" v-if="columns[22].visible"
                       prop="userName"
      />
      <el-table-column label="创建时间" align="center" v-if="columns[23].visible" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新人" :show-overflow-tooltip="true" align="center" v-if="columns[24].visible"
                       prop="updateBy"
      />
      <el-table-column label="更新时间" align="center" v-if="columns[25].visible" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{m}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" :show-overflow-tooltip="true" align="center" v-if="columns[26].visible"
                       prop="remark"
      />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manage:orderInfo:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manage:orderInfo:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1280px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="40">
          <el-col :span="8">
            <el-form-item label="店铺" prop="storeId">
              <el-select
                v-model="form.storeId"
                filterable
                remote
                reserve-keyword
                placeholder="请输入店铺名称"
                :remote-method="selectStoreInfoList"
                :loading="storeInfoLoading"
              >
                <el-option
                  v-for="item in storeInfoList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订单号" prop="amazonOrderId">
              <el-input v-model="form.amazonOrderId" placeholder="请输入订单号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="获取订单信息" prop="getOrderInfoByApi">
              <el-button type="primary" @click="getOrderInfoByApiInfo">获取订单信息</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="平台" prop="platform">
              <el-input v-model="form.platform" placeholder="请输入平台"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="站点" prop="marketplaceId">
              <el-input readonly v-model="form.marketplaceId" placeholder="请输入站点"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="订购时间" prop="purchaseDate">
              <el-input readonly v-model="form.purchaseDate" placeholder="请输入订购时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="ASIN" prop="asin">
              <el-input readonly v-model="form.asin" placeholder="请输入ASIN"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品名" prop="title">
              <el-input readonly v-model="form.title" placeholder="请输入品名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="亚马逊评价时间" prop="evaluateTime">
              <el-date-picker clearable
                              v-model="form.evaluateTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择亚马逊评价时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="亚马逊评分星级" prop="evaluateLevel">
              <el-input v-model="form.evaluateLevel" placeholder="请输入亚马逊评分星级"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="亚马逊评价内容">
              <el-input type="textarea" v-model="form.evaluateContent" :min-height="192"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="亚马逊评价链接" prop="comment">
              <el-input v-model="form.comment" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="亚马逊商品链接" prop="goodsLink">
              <el-input v-model="form.goodsLink" placeholder="请输入亚马逊商品链接"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="扫码时间" prop="scanTime">
              <el-date-picker clearable
                              v-model="form.scanTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:dd"
                              placeholder="请选择扫码时间"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买家姓名" prop="buyerName">
              <el-input v-model="form.buyerName" placeholder="请输入买家姓名"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买家邮箱" prop="buyerEmail">
              <el-input v-model="form.buyerEmail" placeholder="请输入买家邮箱"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买家评分" prop="buyerLevel">
              <el-input v-model="form.buyerLevel" placeholder="请输入买家评分"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买家评论" prop="buyerEvaluate">
              <el-input v-model="form.buyerEvaluate" placeholder="请输入买家评论"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求评论状态" prop="begEvaluateStatus">
              <el-radio-group v-model="form.begEvaluateStatus">
                <el-radio
                  v-for="dict in dict.type.beg_evaluate_status"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="售后标记" prop="afterSaleSign">
              <el-radio-group v-model="form.afterSaleSign">
                <el-radio
                  v-for="dict in dict.type.after_sale_sign"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求评论内容">
              <el-input type="textarea" v-model="form.begEvalueteContent" :min-height="192"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评论负责人" prop="evaluatePrincipal">
              <el-input v-model="form.evaluatePrincipal" placeholder="请输入评论负责人"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listOrderInfo,
  getOrderInfo,
  delOrderInfo,
  addOrderInfo,
  updateOrderInfo,
  getOrderInfoByApi
} from '@/api/manage/orderInfo'
import { listStoreInfo } from '@/api/manage/storeInfo'

export default {
  name: 'OrderInfo',
  dicts: ['beg_evaluate_status', 'after_sale_sign'],
  data() {
    return {
      //店铺信息
      storeInfoList: [],
      storeInfoLoading: false,
      storeInfoQueryParams: {
        pageNum: 1,
        pageSize: 100,
        name: ''
      },
      //表格展示列
      columns: [
        { key: 0, label: '编号', visible: false },
        { key: 1, label: '平台', visible: true },
        { key: 2, label: '店铺', visible: true },
        { key: 3, label: '站点', visible: true },
        { key: 4, label: '订单号', visible: true },
        { key: 5, label: '订购时间', visible: true },
        { key: 6, label: 'ASIN', visible: true },
        { key: 7, label: '品名', visible: true },
        { key: 8, label: '亚马逊评价链接', visible: false },
        { key: 9, label: '亚马逊评价时间', visible: false },
        { key: 10, label: '亚马逊评分星级', visible: false },
        { key: 11, label: '亚马逊评价内容', visible: false },
        { key: 12, label: '亚马逊商品链接', visible: false },
        { key: 13, label: '扫码时间', visible: false },
        { key: 14, label: '买家姓名', visible: true },
        { key: 15, label: '买家邮箱', visible: false },
        { key: 16, label: '买家评分', visible: false },
        { key: 17, label: '买家评论', visible: false },
        { key: 18, label: '请求评论状态', visible: false },
        { key: 19, label: '请求评论内容', visible: false },
        { key: 20, label: '评论负责人', visible: true },
        { key: 21, label: '售后标记', visible: false },
        { key: 22, label: '创建人', visible: false },
        { key: 23, label: '创建时间', visible: false },
        { key: 24, label: '更新人', visible: false },
        { key: 25, label: '更新时间', visible: false },
        { key: 26, label: '备注', visible: false }
      ],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 订单表格数据
      orderInfoList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeScanTime: [],
      // 备注时间范围
      daterangeCreateTime: [],
      // 备注时间范围
      daterangeUpdateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        platform: null,
        storeId: null,
        marketplaceId: null,
        amazonOrderId: null,
        purchaseDate: null,
        asin: null,
        title: null,
        scanTime: null,
        buyerName: null,
        buyerEmail: null,
        begEvaluateStatus: null,
        evaluatePrincipal: null,
        afterSaleSign: null,
        createTime: null,
        remark: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userName: [
          { required: true, message: '创建人不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getStoreInfoList()
  },
  methods: {
    /**
     * 获取店铺列表推荐
     * @param query
     */
    selectStoreInfoList(query) {
      if (query !== '') {
        this.storeInfoLoading = true
        this.storeInfoQueryParams.name = query
        setTimeout(() => {
          this.getStoreInfoList()
        }, 200)
      } else {
        this.storeInfoList = []
        this.storeInfoQueryParams.id = null
      }
    },
    /**
     * 获取店铺信息列表
     */
    getStoreInfoList() {
      //添加查询参数
      if (this.form.storeId != null) {
        this.storeInfoQueryParams.id = this.form.storeId
      } else {
        this.storeInfoQueryParams.id = null
      }
      if (this.storeInfoQueryParams.name !== '') {
        this.storeInfoQueryParams.id = null
      }
      this.storeInfoList = []
      listStoreInfo(this.storeInfoQueryParams).then(res => {
        this.storeInfoList = res?.rows
        this.storeInfoLoading = false
      })
    },
    getOrderInfoByApiInfo() {
      const query = {
        storeId: this.form.storeId,
        amazonOrderId: this.form.amazonOrderId
      }
      getOrderInfoByApi(query).then(res => {
        console.log(res)
        this.form.marketplaceId = res?.data?.marketplaceId
        this.form.purchaseDate = res?.data?.purchaseDate
        this.form.asin = res?.data?.asin
        this.form.title = res?.data?.title
      })
    },
    /** 查询订单列表 */
    getList() {
      this.loading = true
      this.queryParams.params = {}
      if (null != this.daterangeScanTime && '' != this.daterangeScanTime) {
        this.queryParams.params['beginScanTime'] = this.daterangeScanTime[0]
        this.queryParams.params['endScanTime'] = this.daterangeScanTime[1]
      }
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params['beginCreateTime'] = this.daterangeCreateTime[0]
        this.queryParams.params['endCreateTime'] = this.daterangeCreateTime[1]
      }
      if (null != this.daterangeUpdateTime && '' != this.daterangeUpdateTime) {
        this.queryParams.params['beginUpdateTime'] = this.daterangeUpdateTime[0]
        this.queryParams.params['endUpdateTime'] = this.daterangeUpdateTime[1]
      }
      listOrderInfo(this.queryParams).then(response => {
        this.orderInfoList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        platform: null,
        storeId: null,
        marketplaceId: null,
        amazonOrderId: null,
        purchaseDate: null,
        asin: null,
        title: null,
        comment: null,
        evaluateTime: null,
        evaluateLevel: null,
        evaluateContent: null,
        goodsLink: null,
        scanTime: null,
        buyerName: null,
        buyerEmail: null,
        buyerLevel: null,
        buyerEvaluate: null,
        begEvaluateStatus: null,
        begEvalueteContent: null,
        evaluatePrincipal: null,
        afterSaleSign: null,
        userName: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeScanTime = []
      this.daterangeCreateTime = []
      this.daterangeUpdateTime = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加订单'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOrderInfo(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改订单'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOrderInfo(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addOrderInfo(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除订单编号为"' + ids + '"的数据项？').then(function() {
        return delOrderInfo(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('manage/orderInfo/export', {
        ...this.queryParams
      }, `orderInfo_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
