# 开发规范指南

## 实体类开发规范

1. **目录结构**: 所有实体类请放置于`entity`目录下，根据不同功能创建不同文件夹，严格遵循player模块的组织方式。

2. **抽象与继承**:
    - 当处理多个相关实体时，创建抽象类以封装共性属性和方法
    - 使用枚举类表示不同属性类型
    - 具体的实体类应继承自相应的抽象类
    - 请参照player模块的实现方式

3. **对象创建**:
    - 使用工厂模式创建对象
    - 在`factory`目录下为每种实体类型创建专属工厂
    - 创建对象时必须通过对应的工厂方法

## 通用规范

4. **共享组件**: 被其他模块使用的实体类请放入`common`目录，例如会被板块调用的`Tile`类。

5. **接口设计**:
    - 当多个对象需要使用相同功能但实现方式不同时，必须定义接口
    - 需要该功能的类通过实现(implements)该接口来提供具体实现

6. **条件判断**: 使用枚举类进行条件判断时，应使用`switch-case`结构而非`if`语句。

7. **工具类**: 所有工具方法请统一放置在`utils`文件夹下。

8. **Lombok使用规范**:
    - 本项目使用lombok注解简化代码
    - 使用lombok后，请勿手动编写setter方法
    - lombok会自动生成setter方法，手动编写会导致覆盖并使lombok生成的方法失效

9. **注释规范**:
    - 代码必须附上完整的中文注释
    - 注释最终将统一转换为英文格式，请避免中英文混合使用
    - 注释应写在代码行上方，不可写在行尾

