// Generated by the protocol buffer compiler.  DO NOT EDIT!

package com.dyuproject.protostuff.model;

public final class V22Lite {
  private V22Lite() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  public static final class Person extends
      com.google.protobuf.GeneratedMessageLite {
    // Use Person.newBuilder() to construct.
    private Person() {}
    
    private static final Person defaultInstance = new Person();
    public static Person getDefaultInstance() {
      return defaultInstance;
    }
    
    public Person getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    // required int32 id = 1;
    public static final int ID_FIELD_NUMBER = 1;
    private boolean hasId;
    private int id_ = 0;
    public boolean hasId() { return hasId; }
    public int getId() { return id_; }
    
    // optional string email = 2;
    public static final int EMAIL_FIELD_NUMBER = 2;
    private boolean hasEmail;
    private java.lang.String email_ = "";
    public boolean hasEmail() { return hasEmail; }
    public java.lang.String getEmail() { return email_; }
    
    // optional string first_name = 3;
    public static final int FIRST_NAME_FIELD_NUMBER = 3;
    private boolean hasFirstName;
    private java.lang.String firstName_ = "";
    public boolean hasFirstName() { return hasFirstName; }
    public java.lang.String getFirstName() { return firstName_; }
    
    // optional string lastName = 4;
    public static final int LASTNAME_FIELD_NUMBER = 4;
    private boolean hasLastName;
    private java.lang.String lastName_ = "";
    public boolean hasLastName() { return hasLastName; }
    public java.lang.String getLastName() { return lastName_; }
    
    // repeated .testmodel.Task _delegated_task_ = 5;
    public static final int _DELEGATED_TASK__FIELD_NUMBER = 5;
    private java.util.List<com.dyuproject.protostuff.model.V22Lite.Task> DelegatedTask_ =
      java.util.Collections.emptyList();
    public java.util.List<com.dyuproject.protostuff.model.V22Lite.Task> getDelegatedTaskList() {
      return DelegatedTask_;
    }
    public int getDelegatedTaskCount() { return DelegatedTask_.size(); }
    public com.dyuproject.protostuff.model.V22Lite.Task getDelegatedTask(int index) {
      return DelegatedTask_.get(index);
    }
    
    // repeated .testmodel.Task _priorityTask_ = 6;
    public static final int _PRIORITYTASK__FIELD_NUMBER = 6;
    private java.util.List<com.dyuproject.protostuff.model.V22Lite.Task> PriorityTask_ =
      java.util.Collections.emptyList();
    public java.util.List<com.dyuproject.protostuff.model.V22Lite.Task> getPriorityTaskList() {
      return PriorityTask_;
    }
    public int getPriorityTaskCount() { return PriorityTask_.size(); }
    public com.dyuproject.protostuff.model.V22Lite.Task getPriorityTask(int index) {
      return PriorityTask_.get(index);
    }
    
    // optional int32 _aGe_ = 7;
    public static final int _AGE__FIELD_NUMBER = 7;
    private boolean hasAGe;
    private int AGe_ = 0;
    public boolean hasAGe() { return hasAGe; }
    public int getAGe() { return AGe_; }
    
    public final boolean isInitialized() {
      if (!hasId) return false;
      for (com.dyuproject.protostuff.model.V22Lite.Task element : getDelegatedTaskList()) {
        if (!element.isInitialized()) return false;
      }
      for (com.dyuproject.protostuff.model.V22Lite.Task element : getPriorityTaskList()) {
        if (!element.isInitialized()) return false;
      }
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (hasId()) {
        output.writeInt32(1, getId());
      }
      if (hasEmail()) {
        output.writeString(2, getEmail());
      }
      if (hasFirstName()) {
        output.writeString(3, getFirstName());
      }
      if (hasLastName()) {
        output.writeString(4, getLastName());
      }
      for (com.dyuproject.protostuff.model.V22Lite.Task element : getDelegatedTaskList()) {
        output.writeMessage(5, element);
      }
      for (com.dyuproject.protostuff.model.V22Lite.Task element : getPriorityTaskList()) {
        output.writeMessage(6, element);
      }
      if (hasAGe()) {
        output.writeInt32(7, getAGe());
      }
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, getId());
      }
      if (hasEmail()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getEmail());
      }
      if (hasFirstName()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getFirstName());
      }
      if (hasLastName()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getLastName());
      }
      for (com.dyuproject.protostuff.model.V22Lite.Task element : getDelegatedTaskList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, element);
      }
      for (com.dyuproject.protostuff.model.V22Lite.Task element : getPriorityTaskList()) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, element);
      }
      if (hasAGe()) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(7, getAGe());
      }
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.dyuproject.protostuff.model.V22Lite.Person parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Person parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Person parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Person parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Person parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Person parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Person parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeDelimitedFrom(input).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Person parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeDelimitedFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Person parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Person parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.dyuproject.protostuff.model.V22Lite.Person prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.dyuproject.protostuff.model.V22Lite.Person, Builder> {
      private com.dyuproject.protostuff.model.V22Lite.Person result;
      
      // Construct using com.dyuproject.protostuff.model.V22Lite.Person.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.dyuproject.protostuff.model.V22Lite.Person();
        return builder;
      }
      
      protected com.dyuproject.protostuff.model.V22Lite.Person internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.dyuproject.protostuff.model.V22Lite.Person();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.dyuproject.protostuff.model.V22Lite.Person getDefaultInstanceForType() {
        return com.dyuproject.protostuff.model.V22Lite.Person.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.dyuproject.protostuff.model.V22Lite.Person build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.dyuproject.protostuff.model.V22Lite.Person buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.dyuproject.protostuff.model.V22Lite.Person buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        if (result.DelegatedTask_ != java.util.Collections.EMPTY_LIST) {
          result.DelegatedTask_ =
            java.util.Collections.unmodifiableList(result.DelegatedTask_);
        }
        if (result.PriorityTask_ != java.util.Collections.EMPTY_LIST) {
          result.PriorityTask_ =
            java.util.Collections.unmodifiableList(result.PriorityTask_);
        }
        com.dyuproject.protostuff.model.V22Lite.Person returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.dyuproject.protostuff.model.V22Lite.Person other) {
        if (other == com.dyuproject.protostuff.model.V22Lite.Person.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasEmail()) {
          setEmail(other.getEmail());
        }
        if (other.hasFirstName()) {
          setFirstName(other.getFirstName());
        }
        if (other.hasLastName()) {
          setLastName(other.getLastName());
        }
        if (!other.DelegatedTask_.isEmpty()) {
          if (result.DelegatedTask_.isEmpty()) {
            result.DelegatedTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V22Lite.Task>();
          }
          result.DelegatedTask_.addAll(other.DelegatedTask_);
        }
        if (!other.PriorityTask_.isEmpty()) {
          if (result.PriorityTask_.isEmpty()) {
            result.PriorityTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V22Lite.Task>();
          }
          result.PriorityTask_.addAll(other.PriorityTask_);
        }
        if (other.hasAGe()) {
          setAGe(other.getAGe());
        }
        return this;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              return this;
            default: {
              if (!parseUnknownField(input, extensionRegistry, tag)) {
                return this;
              }
              break;
            }
            case 8: {
              setId(input.readInt32());
              break;
            }
            case 18: {
              setEmail(input.readString());
              break;
            }
            case 26: {
              setFirstName(input.readString());
              break;
            }
            case 34: {
              setLastName(input.readString());
              break;
            }
            case 42: {
              com.dyuproject.protostuff.model.V22Lite.Task.Builder subBuilder = com.dyuproject.protostuff.model.V22Lite.Task.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addDelegatedTask(subBuilder.buildPartial());
              break;
            }
            case 50: {
              com.dyuproject.protostuff.model.V22Lite.Task.Builder subBuilder = com.dyuproject.protostuff.model.V22Lite.Task.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addPriorityTask(subBuilder.buildPartial());
              break;
            }
            case 56: {
              setAGe(input.readInt32());
              break;
            }
          }
        }
      }
      
      
      // required int32 id = 1;
      public boolean hasId() {
        return result.hasId();
      }
      public int getId() {
        return result.getId();
      }
      public Builder setId(int value) {
        result.hasId = true;
        result.id_ = value;
        return this;
      }
      public Builder clearId() {
        result.hasId = false;
        result.id_ = 0;
        return this;
      }
      
      // optional string email = 2;
      public boolean hasEmail() {
        return result.hasEmail();
      }
      public java.lang.String getEmail() {
        return result.getEmail();
      }
      public Builder setEmail(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasEmail = true;
        result.email_ = value;
        return this;
      }
      public Builder clearEmail() {
        result.hasEmail = false;
        result.email_ = getDefaultInstance().getEmail();
        return this;
      }
      
      // optional string first_name = 3;
      public boolean hasFirstName() {
        return result.hasFirstName();
      }
      public java.lang.String getFirstName() {
        return result.getFirstName();
      }
      public Builder setFirstName(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasFirstName = true;
        result.firstName_ = value;
        return this;
      }
      public Builder clearFirstName() {
        result.hasFirstName = false;
        result.firstName_ = getDefaultInstance().getFirstName();
        return this;
      }
      
      // optional string lastName = 4;
      public boolean hasLastName() {
        return result.hasLastName();
      }
      public java.lang.String getLastName() {
        return result.getLastName();
      }
      public Builder setLastName(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasLastName = true;
        result.lastName_ = value;
        return this;
      }
      public Builder clearLastName() {
        result.hasLastName = false;
        result.lastName_ = getDefaultInstance().getLastName();
        return this;
      }
      
      // repeated .testmodel.Task _delegated_task_ = 5;
      public java.util.List<com.dyuproject.protostuff.model.V22Lite.Task> getDelegatedTaskList() {
        return java.util.Collections.unmodifiableList(result.DelegatedTask_);
      }
      public int getDelegatedTaskCount() {
        return result.getDelegatedTaskCount();
      }
      public com.dyuproject.protostuff.model.V22Lite.Task getDelegatedTask(int index) {
        return result.getDelegatedTask(index);
      }
      public Builder setDelegatedTask(int index, com.dyuproject.protostuff.model.V22Lite.Task value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.DelegatedTask_.set(index, value);
        return this;
      }
      public Builder setDelegatedTask(int index, com.dyuproject.protostuff.model.V22Lite.Task.Builder builderForValue) {
        result.DelegatedTask_.set(index, builderForValue.build());
        return this;
      }
      public Builder addDelegatedTask(com.dyuproject.protostuff.model.V22Lite.Task value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.DelegatedTask_.isEmpty()) {
          result.DelegatedTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V22Lite.Task>();
        }
        result.DelegatedTask_.add(value);
        return this;
      }
      public Builder addDelegatedTask(com.dyuproject.protostuff.model.V22Lite.Task.Builder builderForValue) {
        if (result.DelegatedTask_.isEmpty()) {
          result.DelegatedTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V22Lite.Task>();
        }
        result.DelegatedTask_.add(builderForValue.build());
        return this;
      }
      public Builder addAllDelegatedTask(
          java.lang.Iterable<? extends com.dyuproject.protostuff.model.V22Lite.Task> values) {
        if (result.DelegatedTask_.isEmpty()) {
          result.DelegatedTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V22Lite.Task>();
        }
        super.addAll(values, result.DelegatedTask_);
        return this;
      }
      public Builder clearDelegatedTask() {
        result.DelegatedTask_ = java.util.Collections.emptyList();
        return this;
      }
      
      // repeated .testmodel.Task _priorityTask_ = 6;
      public java.util.List<com.dyuproject.protostuff.model.V22Lite.Task> getPriorityTaskList() {
        return java.util.Collections.unmodifiableList(result.PriorityTask_);
      }
      public int getPriorityTaskCount() {
        return result.getPriorityTaskCount();
      }
      public com.dyuproject.protostuff.model.V22Lite.Task getPriorityTask(int index) {
        return result.getPriorityTask(index);
      }
      public Builder setPriorityTask(int index, com.dyuproject.protostuff.model.V22Lite.Task value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.PriorityTask_.set(index, value);
        return this;
      }
      public Builder setPriorityTask(int index, com.dyuproject.protostuff.model.V22Lite.Task.Builder builderForValue) {
        result.PriorityTask_.set(index, builderForValue.build());
        return this;
      }
      public Builder addPriorityTask(com.dyuproject.protostuff.model.V22Lite.Task value) {
        if (value == null) {
          throw new NullPointerException();
        }
        if (result.PriorityTask_.isEmpty()) {
          result.PriorityTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V22Lite.Task>();
        }
        result.PriorityTask_.add(value);
        return this;
      }
      public Builder addPriorityTask(com.dyuproject.protostuff.model.V22Lite.Task.Builder builderForValue) {
        if (result.PriorityTask_.isEmpty()) {
          result.PriorityTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V22Lite.Task>();
        }
        result.PriorityTask_.add(builderForValue.build());
        return this;
      }
      public Builder addAllPriorityTask(
          java.lang.Iterable<? extends com.dyuproject.protostuff.model.V22Lite.Task> values) {
        if (result.PriorityTask_.isEmpty()) {
          result.PriorityTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V22Lite.Task>();
        }
        super.addAll(values, result.PriorityTask_);
        return this;
      }
      public Builder clearPriorityTask() {
        result.PriorityTask_ = java.util.Collections.emptyList();
        return this;
      }
      
      // optional int32 _aGe_ = 7;
      public boolean hasAGe() {
        return result.hasAGe();
      }
      public int getAGe() {
        return result.getAGe();
      }
      public Builder setAGe(int value) {
        result.hasAGe = true;
        result.AGe_ = value;
        return this;
      }
      public Builder clearAGe() {
        result.hasAGe = false;
        result.AGe_ = 0;
        return this;
      }
    }
    
    static {
      com.dyuproject.protostuff.model.V22Lite.internalForceInit();
    }
  }
  
  public static final class Task extends
      com.google.protobuf.GeneratedMessageLite {
    // Use Task.newBuilder() to construct.
    private Task() {}
    
    private static final Task defaultInstance = new Task();
    public static Task getDefaultInstance() {
      return defaultInstance;
    }
    
    public Task getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public enum Status
        implements com.google.protobuf.Internal.EnumLite {
      PENDING(0, 0),
      STARTED(1, 1),
      COMPLETED(2, 2),
      ;
      
      
      public final int getNumber() { return value; }
      
      public static Status valueOf(int value) {
        switch (value) {
          case 0: return PENDING;
          case 1: return STARTED;
          case 2: return COMPLETED;
          default: return null;
        }
      }
      
      public static com.google.protobuf.Internal.EnumLiteMap<Status>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<Status>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<Status>() {
              public Status findValueByNumber(int number) {
                return Status.valueOf(number)
      ;        }
            };
      
      private final int index;
      private final int value;
      private Status(int index, int value) {
        this.index = index;
        this.value = value;
      }
    }
    
    // required int32 _id = 1;
    public static final int _ID_FIELD_NUMBER = 1;
    private boolean hasId;
    private int Id_ = 0;
    public boolean hasId() { return hasId; }
    public int getId() { return Id_; }
    
    // optional string name_ = 2;
    public static final int NAME__FIELD_NUMBER = 2;
    private boolean hasName;
    private java.lang.String name_ = "";
    public boolean hasName() { return hasName; }
    public java.lang.String getName() { return name_; }
    
    // optional string _description_ = 3;
    public static final int _DESCRIPTION__FIELD_NUMBER = 3;
    private boolean hasDescription;
    private java.lang.String Description_ = "";
    public boolean hasDescription() { return hasDescription; }
    public java.lang.String getDescription() { return Description_; }
    
    // optional .testmodel.Task.Status status = 4;
    public static final int STATUS_FIELD_NUMBER = 4;
    private boolean hasStatus;
    private com.dyuproject.protostuff.model.V22Lite.Task.Status status_ = com.dyuproject.protostuff.model.V22Lite.Task.Status.PENDING;
    public boolean hasStatus() { return hasStatus; }
    public com.dyuproject.protostuff.model.V22Lite.Task.Status getStatus() { return status_; }
    
    public final boolean isInitialized() {
      if (!hasId) return false;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (hasId()) {
        output.writeInt32(1, getId());
      }
      if (hasName()) {
        output.writeString(2, getName());
      }
      if (hasDescription()) {
        output.writeString(3, getDescription());
      }
      if (hasStatus()) {
        output.writeEnum(4, getStatus().getNumber());
      }
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (hasId()) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, getId());
      }
      if (hasName()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(2, getName());
      }
      if (hasDescription()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(3, getDescription());
      }
      if (hasStatus()) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(4, getStatus().getNumber());
      }
      memoizedSerializedSize = size;
      return size;
    }
    
    public static com.dyuproject.protostuff.model.V22Lite.Task parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Task parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Task parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Task parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Task parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Task parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Task parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeDelimitedFrom(input).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Task parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeDelimitedFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Task parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.dyuproject.protostuff.model.V22Lite.Task parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.dyuproject.protostuff.model.V22Lite.Task prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.dyuproject.protostuff.model.V22Lite.Task, Builder> {
      private com.dyuproject.protostuff.model.V22Lite.Task result;
      
      // Construct using com.dyuproject.protostuff.model.V22Lite.Task.newBuilder()
      private Builder() {}
      
      private static Builder create() {
        Builder builder = new Builder();
        builder.result = new com.dyuproject.protostuff.model.V22Lite.Task();
        return builder;
      }
      
      protected com.dyuproject.protostuff.model.V22Lite.Task internalGetResult() {
        return result;
      }
      
      public Builder clear() {
        if (result == null) {
          throw new IllegalStateException(
            "Cannot call clear() after build().");
        }
        result = new com.dyuproject.protostuff.model.V22Lite.Task();
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(result);
      }
      
      public com.dyuproject.protostuff.model.V22Lite.Task getDefaultInstanceForType() {
        return com.dyuproject.protostuff.model.V22Lite.Task.getDefaultInstance();
      }
      
      public boolean isInitialized() {
        return result.isInitialized();
      }
      public com.dyuproject.protostuff.model.V22Lite.Task build() {
        if (result != null && !isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return buildPartial();
      }
      
      private com.dyuproject.protostuff.model.V22Lite.Task buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        if (!isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return buildPartial();
      }
      
      public com.dyuproject.protostuff.model.V22Lite.Task buildPartial() {
        if (result == null) {
          throw new IllegalStateException(
            "build() has already been called on this Builder.");
        }
        com.dyuproject.protostuff.model.V22Lite.Task returnMe = result;
        result = null;
        return returnMe;
      }
      
      public Builder mergeFrom(com.dyuproject.protostuff.model.V22Lite.Task other) {
        if (other == com.dyuproject.protostuff.model.V22Lite.Task.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasName()) {
          setName(other.getName());
        }
        if (other.hasDescription()) {
          setDescription(other.getDescription());
        }
        if (other.hasStatus()) {
          setStatus(other.getStatus());
        }
        return this;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              return this;
            default: {
              if (!parseUnknownField(input, extensionRegistry, tag)) {
                return this;
              }
              break;
            }
            case 8: {
              setId(input.readInt32());
              break;
            }
            case 18: {
              setName(input.readString());
              break;
            }
            case 26: {
              setDescription(input.readString());
              break;
            }
            case 32: {
              int rawValue = input.readEnum();
              com.dyuproject.protostuff.model.V22Lite.Task.Status value = com.dyuproject.protostuff.model.V22Lite.Task.Status.valueOf(rawValue);
              if (value != null) {
                setStatus(value);
              }
              break;
            }
          }
        }
      }
      
      
      // required int32 _id = 1;
      public boolean hasId() {
        return result.hasId();
      }
      public int getId() {
        return result.getId();
      }
      public Builder setId(int value) {
        result.hasId = true;
        result.Id_ = value;
        return this;
      }
      public Builder clearId() {
        result.hasId = false;
        result.Id_ = 0;
        return this;
      }
      
      // optional string name_ = 2;
      public boolean hasName() {
        return result.hasName();
      }
      public java.lang.String getName() {
        return result.getName();
      }
      public Builder setName(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasName = true;
        result.name_ = value;
        return this;
      }
      public Builder clearName() {
        result.hasName = false;
        result.name_ = getDefaultInstance().getName();
        return this;
      }
      
      // optional string _description_ = 3;
      public boolean hasDescription() {
        return result.hasDescription();
      }
      public java.lang.String getDescription() {
        return result.getDescription();
      }
      public Builder setDescription(java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  result.hasDescription = true;
        result.Description_ = value;
        return this;
      }
      public Builder clearDescription() {
        result.hasDescription = false;
        result.Description_ = getDefaultInstance().getDescription();
        return this;
      }
      
      // optional .testmodel.Task.Status status = 4;
      public boolean hasStatus() {
        return result.hasStatus();
      }
      public com.dyuproject.protostuff.model.V22Lite.Task.Status getStatus() {
        return result.getStatus();
      }
      public Builder setStatus(com.dyuproject.protostuff.model.V22Lite.Task.Status value) {
        if (value == null) {
          throw new NullPointerException();
        }
        result.hasStatus = true;
        result.status_ = value;
        return this;
      }
      public Builder clearStatus() {
        result.hasStatus = false;
        result.status_ = com.dyuproject.protostuff.model.V22Lite.Task.Status.PENDING;
        return this;
      }
    }
    
    static {
      com.dyuproject.protostuff.model.V22Lite.internalForceInit();
    }
  }
  
  
  static {
  }
  
  public static void internalForceInit() {}
}