<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="唯一标识" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入唯一标识"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入店铺名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="卖家编号" prop="sellerId">
        <el-input
          v-model="queryParams.sellerId"
          placeholder="请输入亚马逊卖家编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="大区" prop="region">
        <el-input
          v-model="queryParams.region"
          placeholder="请输入大区"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="站点ID" prop="marketplaceId">
        <el-input
          v-model="queryParams.marketplaceId"
          placeholder="请输入站点ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="店铺状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择店铺状态" clearable>
          <el-option
            v-for="dict in dict.type.store_status"
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
          v-hasPermi="['manage:storeInfo:add']"
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
          v-hasPermi="['manage:storeInfo:edit']"
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
          v-hasPermi="['manage:storeInfo:remove']"
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
          v-hasPermi="['manage:storeInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="storeInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="唯一标识" align="center" v-if="columns[0].visible" prop="id"/>
      <el-table-column label="店铺名称" :show-overflow-tooltip="true" align="center" v-if="columns[1].visible"
                       prop="name"
      />
      <el-table-column label="亚马逊卖家编号" :show-overflow-tooltip="true" align="center" v-if="columns[2].visible"
                       prop="sellerId"
      />
      <el-table-column label="大区" :show-overflow-tooltip="true" align="center" v-if="columns[3].visible"
                       prop="region"
      />
      <el-table-column label="站点ID" :show-overflow-tooltip="true" align="center" v-if="columns[4].visible"
                       prop="marketplaceId"
      />
      <el-table-column label="广告授权状态" :show-overflow-tooltip="true" align="center" v-if="columns[5].visible"
                       prop="adStatus"
      />
      <el-table-column label="店铺状态" align="center" v-if="columns[6].visible" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.store_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建人" :show-overflow-tooltip="true" align="center" v-if="columns[7].visible"
                       prop="userId"
      />
      <el-table-column label="创建时间" align="center" v-if="columns[8].visible" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新人" :show-overflow-tooltip="true" align="center" v-if="columns[9].visible"
                       prop="updateBy"
      />
      <el-table-column label="更新时间" align="center" v-if="columns[10].visible" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" :show-overflow-tooltip="true" align="center" v-if="columns[11].visible"
                       prop="remark"
      />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['manage:storeInfo:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['manage:storeInfo:remove']"
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

    <!-- 添加或修改店铺信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="店铺名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入店铺名称"/>
        </el-form-item>
        <el-form-item label="亚马逊卖家编号" prop="sellerId">
          <el-input v-model="form.sellerId" placeholder="请输入亚马逊卖家编号"/>
        </el-form-item>
        <el-form-item label="大区" prop="region">
          <el-input v-model="form.region" placeholder="请输入大区"/>
        </el-form-item>
        <el-form-item label="站点ID" prop="marketplaceId">
          <el-input v-model="form.marketplaceId" placeholder="请输入站点ID"/>
        </el-form-item>
        <el-form-item label="广告授权状态" prop="adStatus">
          <el-input v-model="form.adStatus" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="店铺状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.store_status"
              :key="dict.value"
              :label="dict.value"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStoreInfo, getStoreInfo, delStoreInfo, addStoreInfo, updateStoreInfo } from '@/api/manage/storeInfo'

export default {
  name: 'StoreInfo',
  dicts: ['store_status'],
  data() {
    return {
      //表格展示列
      columns: [
        { key: 0, label: '唯一标识', visible: false },
        { key: 1, label: '店铺名称', visible: true },
        { key: 2, label: '亚马逊卖家编号', visible: true },
        { key: 3, label: '大区', visible: true },
        { key: 4, label: '站点ID', visible: true },
        { key: 5, label: '广告授权状态', visible: true },
        { key: 6, label: '店铺状态', visible: true },
        { key: 7, label: '创建人', visible: false },
        { key: 8, label: '创建时间', visible: true },
        { key: 9, label: '更新人', visible: false },
        { key: 10, label: '更新时间', visible: false },
        { key: 11, label: '备注', visible: false }
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
      // 店铺信息表格数据
      storeInfoList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeCreateTime: [],
      // 备注时间范围
      daterangeUpdateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        name: null,
        sellerId: null,
        region: null,
        marketplaceId: null,
        adStatus: null,
        status: null,
        createTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: '店铺名称不能为空', trigger: 'blur' }
        ],
        sellerId: [
          { required: true, message: '亚马逊卖家编号不能为空', trigger: 'blur' }
        ],
        marketplaceId: [
          { required: true, message: '站点ID不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '店铺状态不能为空', trigger: 'change' }
        ],
        userId: [
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
  },
  methods: {
    /** 查询店铺信息列表 */
    getList() {
      this.loading = true
      this.queryParams.params = {}
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params['beginCreateTime'] = this.daterangeCreateTime[0]
        this.queryParams.params['endCreateTime'] = this.daterangeCreateTime[1]
      }
      if (null != this.daterangeUpdateTime && '' != this.daterangeUpdateTime) {
        this.queryParams.params['beginUpdateTime'] = this.daterangeUpdateTime[0]
        this.queryParams.params['endUpdateTime'] = this.daterangeUpdateTime[1]
      }
      listStoreInfo(this.queryParams).then(response => {
        this.storeInfoList = response.rows
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
        name: null,
        sellerId: null,
        region: null,
        marketplaceId: null,
        adStatus: null,
        status: null,
        userId: null,
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
      this.title = '添加店铺信息'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getStoreInfo(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改店铺信息'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStoreInfo(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addStoreInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除店铺信息编号为"' + ids + '"的数据项？').then(function() {
        return delStoreInfo(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('manage/storeInfo/export', {
        ...this.queryParams
      }, `storeInfo_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
